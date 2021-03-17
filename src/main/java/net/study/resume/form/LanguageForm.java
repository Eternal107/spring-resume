package net.study.resume.form;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.study.resume.entity.Language;
import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
public class LanguageForm {
	@Valid
	private List<Language> items;

	public LanguageForm(List<Language> items) {
		this.items = items;
	}

}
