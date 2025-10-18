package com.banana.spring.handle;

/**
 * @Description: Aware接口
 * 设置beanName
 * @author zhaojiulin
 * @Date 2025/10/18 12:38
 * @version 1.0
 */
public interface BeanNameAware {
    /**
     * @description: 设置beanName
     * @author zhaojiulin
     * @param: bean名称
     * @return: void
     * @Date: 2025/10/18 12:41
     */
    void setBeanName(String beanName);
}
