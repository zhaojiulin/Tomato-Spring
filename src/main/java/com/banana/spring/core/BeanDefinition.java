package com.banana.spring.core;

import com.banana.spring.constant.BeanScopeType;

/**
 * bean信息
 * @author banana
 */
public class BeanDefinition {
    /**
     * bean类型
     */
    private Class clazz;
    /**
     * 作用域
     */
    private BeanScopeType scope;

    public BeanDefinition() {
    }

    public BeanDefinition(Class clazz, BeanScopeType scope) {
        this.clazz = clazz;
        this.scope = scope;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public BeanScopeType getScope() {
        return scope;
    }

    public void setScope(BeanScopeType scope) {
        this.scope = scope;
    }
}
