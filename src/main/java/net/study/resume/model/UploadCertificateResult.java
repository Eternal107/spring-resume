package net.study.resume.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UploadCertificateResult extends UploadResult  {

	private String certificateName;

	public UploadCertificateResult(String certificateName, String largeUrl, String smallUrl) {
		super(largeUrl, smallUrl);
		this.certificateName = certificateName;
	}

}
