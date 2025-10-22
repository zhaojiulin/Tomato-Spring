package com.tomato.sprout;

import com.tomato.sprout.anno.Autowired;
import com.tomato.sprout.anno.Component;
import com.tomato.sprout.anno.Scope;
import com.tomato.sprout.constant.BeanScopeType;
import com.tomato.sprout.constant.RequestMethod;
import com.tomato.sprout.core.BeanDefinition;
import com.tomato.sprout.core.CircularDependencyCheck;
import com.tomato.sprout.core.ClassPathScanner;
import com.tomato.sprout.handle.ApplicationContextAware;
import com.tomato.sprout.handle.BeanNameAware;
import com.tomato.sprout.handle.BeanPostProcessor;
import com.tomato.sprout.handle.InitializingBean;
import com.tomato.sprout.web.mapping.HandleMethodMappingHolder;
import com.tomato.sprout.web.mapping.HandlerMethod;
import com.tomato.sprout.web.anno.RequestParam;
import com.tomato.sprout.web.anno.WebController;
import com.tomato.sprout.web.anno.WebRequestMapping;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
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
     * 循环依赖检查类-禁止依赖循环
     */
    private final CircularDependencyCheck circularDependencyCheck = new CircularDependencyCheck();
    private static volatile TomatoApplicationContext instance;
    public TomatoApplicationContext instance() {
        if(instance == null) {
            synchronized (TomatoApplicationContext.class) {
                if(instance == null) {
                    instance = new TomatoApplicationContext();
                }
            }
        }
        return instance;
    }

    /**
     * 扫描bean
     *
     * @param primarySource
     */
    public void scanBeanDefinition(Class<?> primarySource) {
        TomatoBoot componentScan = primarySource.getDeclaredAnnotation(TomatoBoot.class);
        // 扫描路径
        ClassPathScanner classPathScanner = new ClassPathScanner();
        String applicationPath = componentScan.scanBasePackage().isEmpty() ? primarySource.getPackage().getName() : componentScan.scanBasePackage();
        String[] packages = new String[]{applicationPath, TomatoApplicationContext.class.getPackage().getName()};
        Set<Class<?>> classSet = new HashSet<>();
        for (String path : packages) {
            classSet.addAll(classPathScanner.scan(path));
        }
        for (Class<?> clazz : classSet) {
            // 创建beanDefinition
            registerBeanDefinition(clazz);
        }
        // 注册前置后置方法
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
        if (!clazz.isAnnotationPresent(Component.class) && !clazz.isAnnotationPresent(WebController.class)) {
            return;
        }
        String beanName = "";
        // 是否有标记这个类是bean
        if (clazz.isAnnotationPresent(Component.class)) {
            Component componentAnno = clazz.getDeclaredAnnotation(Component.class);
            // beanName获取
            beanName = componentAnno.value();
        }
        if (beanName.isEmpty()) {
            beanName = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
        }
        // 解析类，判断当前bean是单例bean，还是原型bean;创建bean信息BeanDefinition
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

    /**
     * 注册内置BeanPostProcessor
     *
     * @param
     */
    public void registerInternalPostProcessors() {
        List<BeanDefinition> processorBeanDefinition = beanDefinitionMap.values().stream()
                .filter(beanDefinition -> BeanPostProcessor.class.isAssignableFrom(beanDefinition.getClazz()))
                .toList();
        for (BeanDefinition beanDefinition : processorBeanDefinition) {
            Object bean = createBean(beanDefinition.getClazz().getName(), beanDefinition);
            beanPostProcessors.add((BeanPostProcessor) bean);
        }
    }

    /**
     * 根据bean实例化class、属性注入、依赖注入、web映射
     */
    public void refreshBean() {
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            Object bean = createBean(beanName, beanDefinition);
            singletonObjects.put(beanName, bean);
            HandleMethodMappingHolder.getInstance().processController(beanDefinition.getClazz(), bean);
        });
    }

    /**
     * 循环依赖检查
     * bean实例化
     * 循环依赖结束
     * @param beanName
     * @param beanDefinition
     * @return
     */
    @SuppressWarnings("unchecked")
    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        circularDependencyCheck.startCreation(beanName);
        Object bean = doCreateBean(beanName, beanDefinition);
        circularDependencyCheck.endCreation(beanName);
        return bean;
    }

    /**
     * 创建bean
     * 实例化
     * 属性填充/依赖注入
     * 执行Aware实现类方法
     * 执行Bean初始化之前自定义处理
     * 初始化Bean
     * 执行Bean初始化之后自定义处理
     */
    private Object doCreateBean(String beanName, BeanDefinition beanDefinition) {
        Class<?> clazz = beanDefinition.getClazz();
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
            }
            // 执行实现Aware接口和BeanPostProcessor接口方法
            instance = doAwareAndPost(beanName, instance, clazz);
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

    /**
     * Aware接口和BeanPostProcessor接口子类实现
     *
     * @param beanName
     * @param instance
     * @param clazz
     * @return
     */
    private Object doAwareAndPost(String beanName, Object instance, Class<?> clazz) {
        if (instance instanceof BeanNameAware) {
            ((BeanNameAware) instance).setBeanName(beanName);
        }
        if (instance instanceof ApplicationContextAware) {
            ((ApplicationContextAware) instance).setApplicationContext(this);
        }
        boolean assignableFromPostProcessor = BeanPostProcessor.class.isAssignableFrom(clazz);
        // BeanPostProcessor 扩展机制 前置
        if (assignableFromPostProcessor) {
            for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            }
        }
        // 自定义初始化
        if (instance instanceof InitializingBean) {
            ((InitializingBean) instance).afterPropertiesSet();
        }
        // BeanPostProcessor 扩展机制 后置
        if (assignableFromPostProcessor) {
            for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                instance = beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            }
        }
        return instance;
    }

    /**
     * 获取完整的bean
     *
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        if (beanDefinitionMap.containsKey(beanName)) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            // 单例bean从单例池获取
            if (beanDefinition.getScope().equals(BeanScopeType.SINGLETON)) {
                Object o = singletonObjects.get(beanName);
                return Objects.isNull(o) ? createBean(beanName, beanDefinition) : o;
            } else {
                // 原型模式
                return createBean(beanName, beanDefinition);
            }
        } else {
            throw new NullPointerException();
        }
    }
}
