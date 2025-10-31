package com.tomato.sprout.interfaces;

/**
 * @description: bean实例化后的自定义初始化接口
 * 实现接口的afterPropertiesSet方法进行自定义初始化
 * @author zhaojiulin
 * @param: null
 * @return: void
 * @Date: 2025/10/18 12:45
 */
public interface InitializingBean {
    /**
     * @description: 实现类自定义初始化
     * @author zhaojiulin
     * @param: null
     * @return: void
     * @Date: 2025/10/18 12:51
     */
    void afterPropertiesSet();
}
