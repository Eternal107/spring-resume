package net.study.resume.service.image;

import net.study.resume.Constants;
import net.study.resume.annotation.EnableUploadImageTempStorage;
import net.study.resume.aspect.UploadImageTempStorage;
import net.study.resume.component.*;
import net.study.resume.exception.CantCompleteClientRequestException;
import net.study.resume.model.UploadCertificateResult;
import net.study.resume.model.UploadResult;
import net.study.resume.model.UploadTempPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

import static net.study.resume.Constants.UIImageType.AVATARS;
import static net.study.resume.Constants.UIImageType.CERTIFICATES;


@Service
public class ImageProcessorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageProcessorService.class);

	@Autowired
	private ImageFormatConverter pngToJpegImageFormatConverter;
	
	@Autowired
	private ImageResizer imageResizer;

	@Autowired
	private ImageOptimizer imageOptimizer;

	@Autowired
	private FileImageStorageService fileImageStorageService;
	
	@Autowired
	private UploadImageTempStorage uploadImageTempStorage;
	
	@Autowired
	private UploadCertificateLinkManager uploadCertificateLinkManager;
	
	@Autowired
	protected DataBuilder dataBuilder;

	@EnableUploadImageTempStorage
	public UploadResult processNewProfilePhoto(MultipartFile upload) {
		try {
			return processUpload(upload, AVATARS);
		} catch (IOException e) {
			throw new CantCompleteClientRequestException("Can't save profile photo upload: " + e.getMessage(), e);
		}
	}

	@EnableUploadImageTempStorage
	public UploadCertificateResult processNewCertificateImage(MultipartFile upload) {
		try {
			UploadResult photoLinks = processUpload(upload, CERTIFICATES);
			uploadCertificateLinkManager.addImageLinks(photoLinks.getLargeUrl(), photoLinks.getSmallUrl());
			String certificateName = dataBuilder.buildCertificateName(upload.getOriginalFilename());
			return new UploadCertificateResult(certificateName, photoLinks.getLargeUrl(), photoLinks.getSmallUrl());
		} catch (IOException e) {
			throw new CantCompleteClientRequestException("Can't save certificate image upload: " + e.getMessage(), e);
		}
	}
	
	protected UploadResult processUpload(MultipartFile multipartFile, Constants.UIImageType imageType) throws IOException {
		String largePhoto = generateNewFileName();
		String smallPhoto = getSmallImageName(largePhoto);
		UploadTempPath uploadTempPath = getCurrentUploadTempPath();
		transferUploadToFile(multipartFile, uploadTempPath.getLargeImagePath());
		resizeAndOptimizeUpload(uploadTempPath, imageType);
		String largePhotoLink = fileImageStorageService.saveAndReturnImageLink(largePhoto, imageType, uploadTempPath.getLargeImagePath());
		String smallPhotoLink = fileImageStorageService.saveAndReturnImageLink(smallPhoto, imageType, uploadTempPath.getSmallImagePath());
		return new UploadResult(largePhotoLink, smallPhotoLink);
	}
	
	protected void resizeAndOptimizeUpload(UploadTempPath uploadTempPath, Constants.UIImageType imageType) throws IOException {
		imageResizer.resizeImage(uploadTempPath.getLargeImagePath(), uploadTempPath.getSmallImagePath(), imageType.getSmallWidth(), imageType.getSmallHeight());
		//imageOptimizer.optimize(uploadTempPath.getSmallImagePath());
		imageResizer.resizeImage(uploadTempPath.getLargeImagePath(), uploadTempPath.getLargeImagePath(), imageType.getLargeWidth(), imageType.getLargeHeight());
		//imageOptimizer.optimize(uploadTempPath.getLargeImagePath());
	}

	protected String generateNewFileName() {
		return UUID.randomUUID().toString() + ".jpg";
	}

	protected String getSmallImageName(String largePhoto) {
		return largePhoto.replace(".jpg", "-sm.jpg");
	}
	
	protected UploadTempPath getCurrentUploadTempPath(){
		return uploadImageTempStorage.getCurrentUploadTempPath();
	}
	
	protected void transferUploadToFile(MultipartFile uploadPhoto, Path destPath) throws IOException {
		String contentType = uploadPhoto.getContentType();
		LOGGER.debug("Content type for upload {}", contentType);
		uploadPhoto.transferTo(destPath.toFile());
		if (contentType.contains("png")) {
			pngToJpegImageFormatConverter.convertImage(destPath, destPath);
		} else if (!contentType.contains("jpg") && !contentType.contains("jpeg")) {
			throw new CantCompleteClientRequestException("Only png and jpg image formats are supported: Current content type=" + contentType);
		}
	}
}
