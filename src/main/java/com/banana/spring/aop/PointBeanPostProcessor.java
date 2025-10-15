package com.banana.spring.aop;

import com.banana.spring.handle.BeanPostProcessor;

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
