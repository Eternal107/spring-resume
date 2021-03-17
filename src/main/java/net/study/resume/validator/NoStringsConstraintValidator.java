package net.study.resume.validator;

import net.study.resume.annotation.constraints.NoStrings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NoStringsConstraintValidator implements ConstraintValidator<NoStrings,String> {

    private String[] values;

    @Override
    public void initialize(NoStrings constraintAnnotation){
        values = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) {
            return false;
        } else{
            boolean result = true;
            for (String s : values) {
                result &= !value.contains(s);
            }

            return result;
        }
    }

}
