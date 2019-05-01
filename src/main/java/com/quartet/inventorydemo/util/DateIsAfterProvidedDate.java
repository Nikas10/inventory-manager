package com.quartet.inventorydemo.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {DateIsAfterProvidedDateValidator.class})
public @interface DateIsAfterProvidedDate {

  String message() default "Invalid date";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String baseProvidedDateFieldName();

  String testDateFieldName();
}
