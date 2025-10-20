package com.tomato.sprout.aop;

import com.tomato.sprout.handle.BeanPostProcessor;

/****
 * @Description: AOP后置实现
 * @author zhaojiulin
 * @Date 2025/10/18 12:15
 * @version 1.0
 */
public class PointBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {

        return bean;
    }
}
