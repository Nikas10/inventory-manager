package com.quartet.inventorydemo.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UUIDStringValidator.class})
public @interface UUIDString {
    String message() default "Not UUIDString";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
