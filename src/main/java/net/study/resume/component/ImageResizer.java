package net.study.resume.component;

import java.io.IOException;
import java.nio.file.Path;


public interface ImageResizer {

	void resizeImage(Path sourceImageFile, Path destImageFile, int width, int height) throws IOException;
}
