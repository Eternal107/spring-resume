package net.study.resume.validator;

import net.study.resume.annotation.constraints.Adulthood;
import org.springframework.stereotype.Component;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class AdulthoodConstraintValidator implements ConstraintValidator<Adulthood, Date> {
	private int adulthoodAge;
	@Override
	public void initialize(Adulthood constraintAnnotation) {
		this.adulthoodAge = constraintAnnotation.adulthoodAge();
	}

	@Override
	public boolean isValid(Date value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		} else{
			LocalDateTime critical = LocalDateTime.now().minusYears(adulthoodAge);
			return value.before(Date.from(Timestamp.valueOf(critical).toInstant()));
		}
	}
}
