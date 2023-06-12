package com.example.junior.validate;

import lombok.experimental.Delegate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
* @Description: 对集合内所有元素都进行校验
* @Author: Junior
* @Date: 2023/3/10
*/
public class ValidationList<E> implements List<E> {

    @Delegate
    @Valid
    public List<E> list = new ArrayList<>();

    @Override
    public String toString() {
        return list.toString();
    }
}
