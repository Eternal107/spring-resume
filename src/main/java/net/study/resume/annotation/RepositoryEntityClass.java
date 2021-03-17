package net.study.resume.annotation;


import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RepositoryEntityClass {

    Class<?> value();
}
