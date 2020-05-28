package it.pasqualecavallo.videowatch.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class MultimediaUtils {

	public String getAndConvertImageFromVideo(String path) {
		try {
			ProcessBuilder pb = new ProcessBuilder("ffmpeg","-i",path,"-ss","00:00:07.000","-vframes","1",path+"image.jpg");
			Process p = pb.start();
			Integer v = p.waitFor();
			if (v == 0) {
				File file = new File(path + "image.jpg");
				byte[] encoded = Base64.encodeBase64(Files.readAllBytes(file.toPath()));
				file.delete();
				return new String("data:image/jpg;base64,"+new String(encoded));
			}
			return null;
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

}
