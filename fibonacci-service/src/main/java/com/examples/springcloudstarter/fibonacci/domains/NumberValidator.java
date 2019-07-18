package com.examples.springcloudstarter.fibonacci.domains;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class NumberValidator implements ConstraintValidator<ValidNumber, Integer> {
    @Autowired
    Environment env;

    String paramKey;
    Integer minValue;
    Integer maxValue;
    boolean required;

    @Override
    public void initialize(ValidNumber constraintAnnotation) {
        this.paramKey = constraintAnnotation.message();
        this.minValue = env.getProperty(constraintAnnotation.minConfig(), Integer.class);
        this.maxValue = env.getProperty(constraintAnnotation.maxConfig(), Integer.class);
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if(required && value == null) {
            context.buildConstraintViolationWithTemplate(paramKey + ".missing").addConstraintViolation();
            return false;
        } else if(minValue != null && value < minValue) {
            context.buildConstraintViolationWithTemplate(paramKey + ".min").addConstraintViolation();
            return false;
        } else if(maxValue != null && value > maxValue) {
            context.buildConstraintViolationWithTemplate(paramKey + ".max").addConstraintViolation();
            return false;
        }

        return true;
    }
}