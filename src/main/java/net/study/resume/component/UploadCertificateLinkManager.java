package net.study.resume.component;

import net.study.resume.service.image.FileImageStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Component
@Scope(scopeName="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UploadCertificateLinkManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(UploadCertificateLinkManager.class);
	@Autowired
	private transient FileImageStorageService fileImageStorageService;
	
	private List<String> imageLinks;
	protected List<String> getImageLinks(){
		if(imageLinks == null) {
			imageLinks = new ArrayList<>(6);
		}
		return imageLinks;
	}
	
	public final void addImageLinks(String largeImageLink, String smallImageLink) {
		getImageLinks().add(largeImageLink);
		getImageLinks().add(smallImageLink);
	}
	
	public final void clearImageLinks(){
		getImageLinks().clear();
	}
	
	@PreDestroy
	private void preDestroy(){
		if(!getImageLinks().isEmpty()) {
			fileImageStorageService.remove(getImageLinks());
			LOGGER.info("Removed {} temporary images", imageLinks);
		}
	}
}
