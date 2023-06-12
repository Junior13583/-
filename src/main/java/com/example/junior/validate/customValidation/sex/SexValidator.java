package com.example.junior.validate.customValidation.sex;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @Description: 约束校验器
* @Author: Junior
* @Date: 2023/3/10
*/
public class SexValidator implements ConstraintValidator<Sex, String> {

    private static final Pattern PATTERN = Pattern.compile("^男$|^女$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 不为null才进行校验
        if (value != null) {
            Matcher matcher = PATTERN.matcher(value);
            return matcher.find();
        }
        return true;
    }
}
