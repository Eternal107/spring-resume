package net.study.resume.validator;

import net.study.resume.annotation.constraints.FirstFieldLessThanSecond;
import org.springframework.beans.BeanUtils;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;


public class FirstFieldLessThanSecondConstraintValidator implements ConstraintValidator<FirstFieldLessThanSecond, Object> {
	private String firstFieldName;
	private String secondFieldName;
	private String message;
	@Override
	public void initialize(final FirstFieldLessThanSecond constraintAnnotation) {
		firstFieldName = constraintAnnotation.first();
		secondFieldName = constraintAnnotation.second();
		message = constraintAnnotation.message();
	}

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext context) {
		boolean valid = false;

		try {
			Object firstValue  = BeanUtils.getPropertyDescriptor(value.getClass(), firstFieldName).getReadMethod().invoke(value);
			Object secondValue = BeanUtils.getPropertyDescriptor(value.getClass(), secondFieldName).getReadMethod().invoke(value);
			 if(secondValue==null) {
				valid = true;
			} else if(firstValue instanceof Comparable<?> && secondValue instanceof Comparable<?>) {
				valid = ((Comparable<Object>)firstValue).compareTo(secondValue) <= 0;
			} else {
				throw new IllegalArgumentException("first and second fields are not comparable!!!");
			}
		} catch (InvocationTargetException | IllegalAccessException e) {
			valid = false;
		}

		if (!valid){
			context.buildConstraintViolationWithTemplate(message)
					.addPropertyNode(secondFieldName)
					.addConstraintViolation()
					.disableDefaultConstraintViolation();
		}
		return valid;
	}
}
