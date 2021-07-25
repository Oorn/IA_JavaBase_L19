package com.company;

import javax.lang.model.element.ElementKind;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SignCode {
    ElementKind kind();
    long id();
}
