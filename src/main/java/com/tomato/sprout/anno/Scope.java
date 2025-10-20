package com.tomato.sprout.anno;

import com.tomato.sprout.constant.BeanScopeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @Description: bean的作用域注解
 * @author zhaojiulin
 * @Date 2025/10/18 12:31
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {
    BeanScopeType value() default BeanScopeType.SINGLETON;
}
