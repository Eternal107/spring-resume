package net.study.resume.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadResult  {

	private String largeUrl;
	private String smallUrl;

	public UploadResult(String largeUrl, String smallUrl) {
		this.largeUrl = largeUrl;
		this.smallUrl = smallUrl;
	}
}
