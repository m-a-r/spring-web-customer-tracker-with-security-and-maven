package com.luv2code.springdemo.validation;

import lombok.extern.java.Log;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Log
public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String firstFieldName;
    private String secondFieldName;
    private String messge;

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {

        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
        messge = constraintAnnotation.message();

    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {

        boolean valid = true;

        log.info("=====>>> Validating in: " + getClass());
        log.info("=====>>> value.getClass():" + value.getClass());
        log.info("=====>>> value.toString():" + value.toString());

        try {
            final Object firstObject = new BeanWrapperImpl(value).getPropertyValue(firstFieldName);
            final Object secondObject = new BeanWrapperImpl(value).getPropertyValue(secondFieldName);

            valid = ((firstObject == null && secondObject == null) ||
                    (firstObject != null && firstObject.equals(secondObject)));
        } catch (final Exception ignore) {
            // we can ignore
        }

        if (!valid) {
            context.buildConstraintViolationWithTemplate(messge)
                    .addPropertyNode(firstFieldName)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return valid;
    }

}
