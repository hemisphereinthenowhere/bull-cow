package com.example.bullcow.validations;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueDigitsValidator implements ConstraintValidator<UniqueDigits, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        boolean isValid = true;


        for (int i = 0; i < value.length(); i++) {
            if (StringUtils.countMatches(value, value.charAt(i)) != 1) {
                isValid = false;
                break;
            }
        }
        return isValid;
    }
}
