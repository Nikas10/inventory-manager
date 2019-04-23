package com.quartet.inventorydemo.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Date;

public class DateIsAfterProvidedDateValidator implements ConstraintValidator<DateIsAfterProvidedDate, Object> {
    private String baseProvidedDateFieldName;
    private String testDateFieldName;

    @Override
    public void initialize(DateIsAfterProvidedDate constraintAnnotation) {
        this.baseProvidedDateFieldName = constraintAnnotation.baseProvidedDateFieldName();
        this.testDateFieldName = constraintAnnotation.testDateFieldName();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            Object baseFieldValue = getFieldValue(object, this.baseProvidedDateFieldName);
            Object testFieldValue = getFieldValue(object, this.testDateFieldName);
            if (!(baseFieldValue instanceof Date)) {
                throw new NotDateException("field with name " + this.baseProvidedDateFieldName + " is not type of java.util.Date");
            }
            if (!(testFieldValue instanceof Date)) {
                throw new NotDateException("field with name " + this.testDateFieldName + " is not type of java.util.Date");
            }

            Date baseDate = (Date) baseFieldValue;
            Date testDate = (Date) testFieldValue;

            return testDate.after(baseDate);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            String errormessage = "Can not extract field for class: " +
                    object.getClass().getName() +
                    " and field names: " +
                    baseProvidedDateFieldName +
                    " , " +
                    testDateFieldName;

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errormessage);
            return false;
        } catch (NotDateException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage());
            return false;
        }
    }

    private Object getFieldValue(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

    class NotDateException extends Exception {
        NotDateException(String message) {
            super(message);
        }
    }
}
