package com.example.bullcow.validations;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Target({ ElementType.FIELD,
        ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=UniqueDigitsValidator.class)
public @interface UniqueDigits {

    String message() default "Non-unique digits";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
