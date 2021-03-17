package net.study.resume.service.image;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.SneakyThrows;
import net.study.resume.Constants;
import net.study.resume.exception.CantCompleteClientRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Service
public class FileImageStorageService  {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileImageStorageService.class);

	@Value("${media.storage.path}")
	protected String path;

	public String saveAndReturnImageLink(String imageName, Constants.UIImageType imageType, Path tempImageFile) {
		try {
			String imageLink = getImageLink(imageType.getFolderName(), imageName);
			saveImageFile(tempImageFile, getDestinationImageFile(imageLink));
			return imageLink;
		} catch (IOException e) {
			throw new CantCompleteClientRequestException("Can't save image: " + e.getMessage(), e);
		}
	}

	protected String getImageLink(String folderName, String imageName) {
		return "/media/" + folderName + "/" + imageName;
	}

	protected Path getDestinationImageFile(String imageLink) {
		return Paths.get(path + imageLink);
	}

	protected void saveImageFile(Path srcImageFile, Path destinationImageFile) throws IOException {
		Files.move(srcImageFile, destinationImageFile);
	}

	@SneakyThrows
	public void remove(List<String> imageLinks) {
		for (String imageLink : imageLinks) {
			if (StringUtils.isNotBlank(imageLink)) {
				removeImageFile(getDestinationImageFile(imageLink));
			}
		}
	}

	protected void removeImageFile(Path path) {
		try {
			Files.deleteIfExists(path);
			LOGGER.debug("Image file {} removed successful", path);
		} catch (IOException e) {
			LOGGER.error("Can't remove file: " + path, e);
		}
	}

}
