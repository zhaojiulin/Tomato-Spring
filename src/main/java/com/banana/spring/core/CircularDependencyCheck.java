package com.banana.spring.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zhaojiulin
 * @version 1.0
 * @description: 循环依赖检查->禁止循环依赖->代码不应该设计出循环依赖
 * @date 2025/10/19 17:22
 */
public class CircularDependencyCheck {
    /**
     * 创建中bean
     */
    private final ThreadLocal<Set<String>> creationBeans = ThreadLocal.withInitial(HashSet::new);
    /**
     * 正在创建bean检测循环依赖
     * @param beanName
     */
    public void startCreation(String beanName) {
        Set<String> inCreation = creationBeans.get();
        if(inCreation.contains(beanName)){
            StringBuilder stackTrace = new StringBuilder();
            Iterator<String> iterator = inCreation.iterator();
            while(iterator.hasNext()){
                if(!stackTrace.isEmpty()){
                    stackTrace.append("->").append(iterator.next());
                }else {
                    stackTrace.append(iterator.next());
                }
            }
            stackTrace.append("->").append(beanName);
            throw new RuntimeException("发现循环依赖：" + stackTrace);
        }
        inCreation.add(beanName);
    }

    /**
     * 创建后清除
     * @param beanName
     */
    public void endCreation(String beanName) {
        Set<String> inCreation = creationBeans.get();
        inCreation.remove(beanName);
        if(inCreation.isEmpty()){
            creationBeans.remove();
        }
    }
}
