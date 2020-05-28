package it.pasqualecavallo.videowatch.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class MultipartFileSender {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final int DEFAULT_BUFFER_SIZE = 20480; // ..bytes = 20KB.
	private static final long DEFAULT_EXPIRE_TIME = 604800000L; // ..ms = 1 week.
	private static final String MULTIPART_BOUNDARY = "MULTIPART_BYTERANGES";

	Path filepath;
	HttpServletRequest request;
	HttpServletResponse response;

	public MultipartFileSender() {
	}

	public static MultipartFileSender fromPath(Path path) {
		return new MultipartFileSender().setFilepath(path);
	}

	public static MultipartFileSender fromFile(File file) {
		return new MultipartFileSender().setFilepath(file.toPath());
	}

	public static MultipartFileSender fromURIString(String uri) {
		return new MultipartFileSender().setFilepath(Paths.get(uri));
	}

	private MultipartFileSender setFilepath(Path filepath) {
		this.filepath = filepath;
		return this;
	}

	public MultipartFileSender with(HttpServletRequest httpRequest) {
		request = httpRequest;
		return this;
	}
	public MultipartFileSender with(HttpServletResponse httpResponse) {
		response = httpResponse;
		return this;
	}
	public void serveResource() throws Exception {
		if (response == null || request == null) {
			return;
		}
		if (!Files.exists(filepath)) {
			logger.error("File doesn't exist at URI : {}", filepath.toAbsolutePath().toString());
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		Long length = Files.size(filepath);
		String fileName = filepath.getFileName().toString();
		FileTime lastModifiedObj = Files.getLastModifiedTime(filepath);
		if (StringUtils.isEmpty(fileName) || lastModifiedObj == null) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		long lastModified = LocalDateTime
				.ofInstant(lastModifiedObj.toInstant(), ZoneId.of(ZoneOffset.systemDefault().getId()))
				.toEpochSecond(ZoneOffset.UTC);
		String contentType = "application/octet-stream";
		String ifNoneMatch = request.getHeader("If-None-Match");
		if (ifNoneMatch != null && HttpUtils.matches(ifNoneMatch, fileName)) {
			response.setHeader("ETag", fileName); // Required in 304.
			response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
			return;
		}
		long ifModifiedSince = request.getDateHeader("If-Modified-Since");
		if (ifNoneMatch == null && ifModifiedSince != -1 && ifModifiedSince + 1000 > lastModified) {
			response.setHeader("ETag", fileName); // Required in 304.
			response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
			return;
		}
		String ifMatch = request.getHeader("If-Match");
		if (ifMatch != null && !HttpUtils.matches(ifMatch, fileName)) {
			response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
			return;
		}
		long ifUnmodifiedSince = request.getDateHeader("If-Unmodified-Since");
		if (ifUnmodifiedSince != -1 && ifUnmodifiedSince + 1000 <= lastModified) {
			response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
			return;
		}
		Range full = new Range(0, length - 1, length);
		List<Range> ranges = new ArrayList<>();
		String range = request.getHeader("Range");
		if (range != null) {
			if (!range.matches("^bytes=\\d*-\\d*(,\\d*-\\d*)*$")) {
				response.setHeader("Content-Range", "bytes */" + length); // Required in 416.
				response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
				return;
			}

			String ifRange = request.getHeader("If-Range");
			if (ifRange != null && !ifRange.equals(fileName)) {
				try {
					long ifRangeTime = request.getDateHeader("If-Range"); // Throws IAE if invalid.
					if (ifRangeTime != -1) {
						ranges.add(full);
					}
				} catch (IllegalArgumentException ignore) {
					ranges.add(full);
				}
			}
			if (ranges.isEmpty()) {
				for (String part : range.substring(6).split(",")) {
					long start = Range.sublong(part, 0, part.indexOf("-"));
					long end = Range.sublong(part, part.indexOf("-") + 1, part.length());
					if (start == -1) {
						start = length - end;
						end = length - 1;
					} else if (end == -1 || end > length - 1) {
						end = length - 1;
					}
					if (start > end) {
						response.setHeader("Content-Range", "bytes */" + length); // Required in 416.
						response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
						return;
					}
					ranges.add(new Range(start, end, length));
				}
			}
		}
		String disposition = "inline";
		if (contentType == null) {
			contentType = "application/octet-stream";
		} else if (!contentType.startsWith("image")) {
			String accept = request.getHeader("Accept");
			disposition = accept != null && HttpUtils.accepts(accept, contentType) ? "inline" : "attachment";
		}
		logger.debug("Content-Type : {}", contentType);
		response.reset();
		response.setBufferSize(DEFAULT_BUFFER_SIZE);
		response.setHeader("Content-Type", contentType);
		response.setHeader("Content-Disposition", disposition + ";filename=\"" + fileName + "\"");
		logger.debug("Content-Disposition : {}", disposition);
		response.setHeader("Accept-Ranges", "bytes");
		response.setHeader("ETag", fileName);
		response.setDateHeader("Last-Modified", lastModified);
		response.setDateHeader("Expires", System.currentTimeMillis() + DEFAULT_EXPIRE_TIME);

		try (InputStream input = new BufferedInputStream(Files.newInputStream(filepath));
				OutputStream output = response.getOutputStream()) {

			if (ranges.isEmpty() || ranges.get(0) == full) {
				logger.info("Return full file");
				response.setContentType(contentType);
				response.setHeader("Content-Range", "bytes " + full.start + "-" + full.end + "/" + full.total);
				response.setHeader("Content-Length", String.valueOf(full.length));
				Range.copy(input, output, length, full.start, full.length);
			} else if (ranges.size() == 1) {
				Range r = ranges.get(0);
				logger.info("Return 1 part of file : from ({}) to ({})", r.start, r.end);
				response.setContentType(contentType);
				response.setHeader("Content-Range", "bytes " + r.start + "-" + r.end + "/" + r.total);
				response.setHeader("Content-Length", String.valueOf(r.length));
				response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.
				Range.copy(input, output, length, r.start, r.length);
			} else {
				response.setContentType("multipart/byteranges; boundary=" + MULTIPART_BOUNDARY);
				response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.
				ServletOutputStream sos = (ServletOutputStream) output;
				for (Range r : ranges) {
					logger.info("Return multi part of file : from ({}) to ({})", r.start, r.end);
					sos.println();
					sos.println("--" + MULTIPART_BOUNDARY);
					sos.println("Content-Type: " + contentType);
					sos.println("Content-Range: bytes " + r.start + "-" + r.end + "/" + r.total);
					Range.copy(input, output, length, r.start, r.length);
				}
				sos.println();
				sos.println("--" + MULTIPART_BOUNDARY + "--");
			}
		}

	}

	private static class Range {
		long start;
		long end;
		long length;
		long total;

		/**
		 * Construct a byte range.
		 * 
		 * @param start Start of the byte range.
		 * @param end   End of the byte range.
		 * @param total Total length of the byte source.
		 */
		public Range(long start, long end, long total) {
			this.start = start;
			this.end = end;
			this.length = end - start + 1;
			this.total = total;
		}

		public static long sublong(String value, int beginIndex, int endIndex) {
			String substring = value.substring(beginIndex, endIndex);
			return (substring.length() > 0) ? Long.parseLong(substring) : -1;
		}

		private static void copy(InputStream input, OutputStream output, long inputSize, long start, long length)
				throws IOException {
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int read;
			if (inputSize == length) {
				while ((read = input.read(buffer)) > 0) {
					output.write(buffer, 0, read);
					output.flush();
				}
			} else {
				input.skip(start);
				long toRead = length;

				while ((read = input.read(buffer)) > 0) {
					if ((toRead -= read) > 0) {
						output.write(buffer, 0, read);
						output.flush();
					} else {
						output.write(buffer, 0, (int) toRead + read);
						output.flush();
						break;
					}
				}
			}
		}
	}

	private static class HttpUtils {

		/**
		 * Returns true if the given accept header accepts the given value.
		 * 
		 * @param acceptHeader The accept header.
		 * @param toAccept     The value to be accepted.
		 * @return True if the given accept header accepts the given value.
		 */
		public static boolean accepts(String acceptHeader, String toAccept) {
			String[] acceptValues = acceptHeader.split("\\s*(,|;)\\s*");
			Arrays.sort(acceptValues);

			return Arrays.binarySearch(acceptValues, toAccept) > -1
					|| Arrays.binarySearch(acceptValues, toAccept.replaceAll("/.*$", "/*")) > -1
					|| Arrays.binarySearch(acceptValues, "*/*") > -1;
		}

		/**
		 * Returns true if the given match header matches the given value.
		 * 
		 * @param matchHeader The match header.
		 * @param toMatch     The value to be matched.
		 * @return True if the given match header matches the given value.
		 */
		public static boolean matches(String matchHeader, String toMatch) {
			String[] matchValues = matchHeader.split("\\s*,\\s*");
			Arrays.sort(matchValues);
			return Arrays.binarySearch(matchValues, toMatch) > -1 || Arrays.binarySearch(matchValues, "*") > -1;
		}
	}
}