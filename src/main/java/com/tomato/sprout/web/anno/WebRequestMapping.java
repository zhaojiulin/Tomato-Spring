package com.tomato.sprout.web.anno;

import com.tomato.sprout.constant.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 请求和响应类中方法的请求路径和请求方式标识
 * @author zhaojiulin
 * @Date 2025/10/18 12:54
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface WebRequestMapping {
    String value() default "";
    RequestMethod method() default RequestMethod.GET;

}
