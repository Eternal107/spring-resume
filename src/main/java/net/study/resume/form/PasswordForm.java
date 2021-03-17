package net.study.resume.form;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.study.resume.annotation.constraints.FieldMatch;
import net.study.resume.annotation.constraints.PasswordStrength;

@FieldMatch.List({
		@FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
})
@Getter
@Setter
public class PasswordForm {

	@PasswordStrength
	private String password;
	
	private String confirmPassword;
	
}
