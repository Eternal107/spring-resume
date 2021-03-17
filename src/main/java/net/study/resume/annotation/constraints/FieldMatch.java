package net.study.resume.annotation.constraints;


import net.study.resume.validator.FieldMatchConstraintValidator;
import net.study.resume.validator.FieldMatchValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * https://github.com/Rudeg/Spring-MVC-Example/blob/master/Lab%202/src/main/java
 * /com/springexample/common/constraits/FieldMatch.java
 * 
 * Validation annotation to validate that 2 fields have the same value. An array
 * of fields and their matching confirmation fields can be supplied.
 *
 * Example, compare 1 pair of fields:
 * 
 * @FieldMatch(first = "password", second = "confirmPassword", message ="The password fields must match")
 *
 * Example, compare more than 1 pair of fields: 
 * @FieldMatch.List({ @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match"),
 * 					  @FieldMatch(first = "email", second = "confirmEmail", message ="The email fields must match")})
 */

@Constraint(validatedBy = FieldMatchValidator.class)
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldMatch {
	String message() default "";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

	String first();
	String second();

	@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List
	{
		FieldMatch[] value();
	}
}
