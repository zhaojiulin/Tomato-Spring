package com.tomato.sprout.handle;
/**
 * @Description: bean自定义初始化前后接口实现
 * 实现InitializingBean接口自定义初始化
 * 初始化前 postProcessBeforeInitialization
 * 初始化 InitializingBean afterPropertiesSet
 * 初始化后 postProcessAfterInitialization
 * @author zhaojiulin
 * @Date 2025/10/18 12:41
 * @version 1.0
 */
public interface BeanPostProcessor {
    /**
     * @description: bean的初始化前自定义处理
     * @author zhaojiulin
     * @param: bean：实例 beanName：bean名称
     * @return:  自定义后的实例
     * @Date: 2025/10/18 12:44
     */
    Object postProcessBeforeInitialization(Object bean, String beanName);
    /**
     * @description: bean的初始化后自定义处理
     * @author zhaojiulin
     * @param: bean：实例 beanName：bean名称
     * @return:  自定义后的实例
     * @Date: 2025/10/18 12:44
     */
    Object postProcessAfterInitialization(Object bean, String beanName);
}
