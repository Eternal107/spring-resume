package net.study.resume.annotation.constraints;

import net.study.resume.validator.NoStringsConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.Valid;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy={NoStringsConstraintValidator.class})
public @interface NoStrings {

    String[] value() default { };

    String message() default "forbidden symbols entered";

    Class<? extends Payload>[] payload() default { };

    Class<?>[] groups() default { };
}
