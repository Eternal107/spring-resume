package net.study.resume.component;

import java.io.IOException;
import java.nio.file.Path;


public interface ImageFormatConverter {

	void convertImage( Path sourceImageFile,Path destImageFile) throws IOException;
}
