package com.tomato.sprout.web.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 负责处理用户请求和响应类标识
 * @author zhaojiulin
 * @Date 2025/10/18 12:52
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebController {
    String value() default "";
}
