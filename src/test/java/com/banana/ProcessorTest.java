package com.banana;

import com.banana.spring.anno.Component;
import com.banana.spring.handle.BeanPostProcessor;

import java.util.Locale;

@Component
public class ProcessorTest implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("初始化前");
        if(UserService.class.getSimpleName().toLowerCase(Locale.ROOT).equals(beanName.toLowerCase(Locale.ROOT))) {
            ((UserService)bean).setName("hahahah");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("初始化后");
        return bean;
    }
}
