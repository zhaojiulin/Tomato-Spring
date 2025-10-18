package com.banana.spring.core;

import com.banana.spring.constant.BeanScopeType;

/**
 * @Description: bean的初始化信息
 * class 类的字节码
 * scope bean的作用域
 * @author zhaojiulin
 * @Date 2025/10/18 12:33
 * @version 1.0
 */
public class BeanDefinition {
    /**
     * bean类型
     */
    private Class<?> clazz;
    /**
     * 作用域
     */
    private BeanScopeType scope;

    public BeanDefinition() {
    }

    public BeanDefinition(Class<?> clazz, BeanScopeType scope) {
        this.clazz = clazz;
        this.scope = scope;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public BeanScopeType getScope() {
        return scope;
    }

    public void setScope(BeanScopeType scope) {
        this.scope = scope;
    }
}
