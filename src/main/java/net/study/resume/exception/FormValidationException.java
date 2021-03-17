package net.study.resume.exception;

import net.study.resume.Constants;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.FieldError;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormValidationException extends DataAccessException {
	private final String fieldName;
	private final Object rejectedValue;
	private final String errorCode;
	private final String message;

	public FormValidationException(String fieldName,Object rejectedValue,String message, String errorCode) {
		super("");
		this.fieldName = fieldName;
		this.rejectedValue = rejectedValue;
		this.errorCode = errorCode;
		this.message = message;
	}

	public FormValidationException(String fieldName,Object rejectedValue,String message) {
		this(fieldName,rejectedValue,message,null);
	}

	public FormValidationException(String fieldName,Object rejectedValue) {
		this(fieldName,rejectedValue,null,null);
	}
	
	public FieldError buildFieldError(String formName) {
		List<String> codes = new ArrayList<>(3);
		if(errorCode != null) {
			codes.add(errorCode);
		}
		codes.addAll(Arrays.asList(FormValidationException.class.getSimpleName()+"."+formName+"."+fieldName,
				formName+"."+fieldName,
				fieldName));
		return new FieldError(formName, fieldName, rejectedValue, false, codes.toArray(Constants.EMPTY_ARRAY), Constants.EMPTY_ARRAY , toString());
	}
	@Override
	public String toString() {
		return message;
	}
}
