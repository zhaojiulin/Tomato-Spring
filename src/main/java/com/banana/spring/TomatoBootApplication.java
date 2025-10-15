package com.banana.spring;

import com.banana.spring.web.EmbeddedServer;
import com.banana.spring.web.servlet.TomcatEmbeddedServer;

public class TomatoBootApplication {
    public static TomatoApplicationContext run(Class<?> primarySource, String... args) {
        // 创建上下文
        TomatoApplicationContext context = new TomatoApplicationContext();
        // 扫描并注册bean
        context.scanBeanDefinition(primarySource);
        // 初始化bean
        context.refreshBean();
        // 启动内嵌服务器
        startTomcatServer(context);
        return context;
    }

    private static void startTomcatServer(TomatoApplicationContext context) {

    }

}
