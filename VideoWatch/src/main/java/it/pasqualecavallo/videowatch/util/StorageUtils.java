package it.pasqualecavallo.videowatch.util;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class StorageUtils {

	@Value("${storage.path}")
	private String storagePath;
	
	
	public String persistFile(MultipartFile file, String name) throws IOException {
		String saveTo = storagePath+name;
		file.transferTo(new File(saveTo));
		return saveTo;
	}
	
	
	
}
