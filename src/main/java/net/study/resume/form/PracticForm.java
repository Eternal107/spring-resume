package net.study.resume.form;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.study.resume.entity.Practic;
import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
public class PracticForm {
	@Valid
	private List<Practic> items;

	public PracticForm(List<Practic> items) {
		this.items = items;
	}

}
