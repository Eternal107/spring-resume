package net.study.resume.model;

import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
public class UploadTempPath {
	private final Path largeImagePath;
	private final Path smallImagePath;
	public UploadTempPath() throws IOException {
		this.largeImagePath = Files.createTempFile("large", ".jpg");
		this.smallImagePath = Files.createTempFile("small", ".jpg");
	}
}
