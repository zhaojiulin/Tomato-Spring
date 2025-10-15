package com.banana.spring.anno;

import com.banana.spring.constant.BeanScopeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {
    BeanScopeType value() default BeanScopeType.SINGLETON;
}
