package com.tomato.sprout;

import com.tomato.sprout.web.serve.TomcatEmbeddedServer;
/**
 * @Description: 容器启动
 * @author zhaojiulin
 * @Date 2025/10/18 14:25
 * @version 1.0
 */
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

    /**
     * 启动服务器 暂时tomcat
     * @param context 容器上下文
     */
    private static void startTomcatServer(TomatoApplicationContext context) {
        TomcatEmbeddedServer tomcatEmbeddedServer = new TomcatEmbeddedServer(context);
        tomcatEmbeddedServer.start();
    }

}
