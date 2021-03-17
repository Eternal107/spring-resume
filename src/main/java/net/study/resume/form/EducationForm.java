package net.study.resume.form;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.study.resume.entity.Education;
import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
public class EducationForm {
	@Valid
	private List<Education> items;

	public EducationForm(List<Education> items) {
		this.items = items;
	}

}
