package com.banana.spring;

import com.banana.spring.anno.Autowired;
import com.banana.spring.anno.Component;
import com.banana.spring.anno.Scope;
import com.banana.spring.constant.BeanScopeType;
import com.banana.spring.core.BeanDefinition;
import com.banana.spring.core.ClassPathScanner;
import com.banana.spring.handle.ApplicationContextAware;
import com.banana.spring.handle.BeanNameAware;
import com.banana.spring.handle.BeanPostProcessor;
import com.banana.spring.handle.InitializingBean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class TomatoApplicationContext {
    /**
     * 单例池
     */
    private final ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();
    /**
     * BeanDefinition定义
     */
    private final ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    /**
     * 实例化BeanPostProcessor
     */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    /**
     * 扫描bean
     *
     * @param primarySource
     */
    public void scanBeanDefinition(Class<?> primarySource) {
        TomatoBoot componentScan = primarySource.getDeclaredAnnotation(TomatoBoot.class);
        // 扫描路径
        ClassPathScanner classPathScanner = new ClassPathScanner();
        Set<Class<?>> classSet = classPathScanner.scan(componentScan.scanBasePackage().isEmpty() ? primarySource.getPackage().getName() : componentScan.scanBasePackage());
        for (Class<?> clazz : classSet) {
            registerBeanDefinition(clazz);
        }
        registerInternalPostProcessors();
    }

    /**
     * 获取BeanDefinition
     *
     * @param clazz
     */
    private void registerBeanDefinition(Class<?> clazz) {
        if (clazz.isAnnotation() || clazz.isInterface()) {
            return;
        }
        // 是否有标记这个类是bean
        if (clazz.isAnnotationPresent(Component.class)) {
            // 解析类，判断当前bean是单例bean，还是原型bean;创建bean信息BeanDefinition
            Component componentAnno = clazz.getDeclaredAnnotation(Component.class);
            // beanName获取
            String beanName = componentAnno.value();
            if (beanName.isEmpty()) {
                beanName = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
            }
            BeanDefinition beanDefinition = new BeanDefinition();
            beanDefinition.setClazz(clazz);
            if (clazz.isAnnotationPresent(Scope.class)) {
                Scope scopeAnno = clazz.getDeclaredAnnotation(Scope.class);
                beanDefinition.setScope(scopeAnno.value());
            } else {
                beanDefinition.setScope(BeanScopeType.SINGLETON);
            }
            beanDefinitionMap.put(beanName, beanDefinition);
        }
    }

    /**
     * 注册内置BeanPostProcessor
     *
     * @param
     */
    public void registerInternalPostProcessors() {
        beanDefinitionMap.forEach((key, value) -> {
            Class clazz = value.getClazz();
            // 判断是否实现自BeanPostProcessor
            if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                BeanPostProcessor newInstance = null;
                try {
                    newInstance = (BeanPostProcessor) clazz.getDeclaredConstructor().newInstance();
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                beanPostProcessors.add(newInstance);
            }
        });

    }

    /**
     * 根据bean实例化class
     */
    public void refreshBean() {
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            Object bean = createBean(beanName, beanDefinition);
            singletonObjects.put(beanName, bean);
        });
    }

    /**
     * 创建bean
     * 实例化
     * 属性填充/依赖注入
     * 执行Aware实现类方法
     * 执行Bean初始化之前自定义处理
     * 初始化Bean
     * 执行Bean初始化之后自定义处理
     *
     * @param beanName
     * @param beanDefinition
     * @return
     */
    @SuppressWarnings("unchecked")
    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getClazz();
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            // 依赖注入
            for (Field declaredField : clazz.getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(Autowired.class)) {
                    Object bean = getBean(declaredField.getName());
                    Autowired autowiredAnno = declaredField.getDeclaredAnnotation(Autowired.class);
                    if (bean == null && autowiredAnno.required()) {
                        throw new NullPointerException();
                    }
                    declaredField.setAccessible(true);
                    declaredField.set(instance, bean);
                }

                if (instance instanceof BeanNameAware) {
                    ((BeanNameAware) instance).setBeanName(beanName);
                }
                if (instance instanceof ApplicationContextAware) {
                    ((ApplicationContextAware) instance).setApplicationContext(this);
                }
                // BeanPostProcessor 扩展机制
                for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                    instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
                }
                // 初始化
                if (instance instanceof InitializingBean) {
                    ((InitializingBean) instance).afterPropertiesSet();
                }
                // BeanPostProcessor 扩展机制
                for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                    instance = beanPostProcessor.postProcessAfterInitialization(instance, beanName);
                }
            }
            return instance;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getBean(String beanName) {
        if (beanDefinitionMap.containsKey(beanName)) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            // 单例bean从单例池获取
            if (beanDefinition.getScope().equals(BeanScopeType.SINGLETON)) {
                return singletonObjects.get(beanName);
            } else {
                // 原型模式
                return createBean(beanName, beanDefinition);
            }
        } else {
            throw new NullPointerException();
        }
    }
}
