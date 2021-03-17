package net.study.resume.form;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.study.resume.entity.Course;
import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
public class CourseForm {
	@Valid
	private List<Course> items ;
	
	public CourseForm(List<Course> items) {
		this.items = items;
	}

}
