package br.com.devbean.annotations;

import br.com.devbean.constants.ProgressStep;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProgressStatus {

    ProgressStep step();

}
