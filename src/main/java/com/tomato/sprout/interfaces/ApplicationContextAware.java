package com.tomato.sprout.interfaces;

import com.tomato.sprout.TomatoApplicationContext;
/**
 * @Description: Aware接口
 * 用于实现该接口的类设置容器上下文
 * @author zhaojiulin
 * @Date 2025/10/18 12:36
 * @version 1.0
 */
public interface ApplicationContextAware {
    /** 
     * @description: 设置容器的上下文
     * @author zhaojiulin
     * @param: 上下信息
     * @return:  VOID
     * @date:  12:36
     */ 
    void setApplicationContext(TomatoApplicationContext applicationContext);
}
