package net.study.resume.form;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.study.resume.annotation.constraints.EnglishLanguage;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignUpForm extends PasswordForm {
	@Size(max = 100)
	@Email
	private String email;
}
