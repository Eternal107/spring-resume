package net.study.resume.form;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.study.resume.entity.Certificate;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CertificateForm {

	@Valid
	private List<Certificate> items;
	
	public CertificateForm(List<Certificate> items) {
		this.items = items;
	}

}
