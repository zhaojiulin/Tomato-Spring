package com.banana.spring.web.servlet;

import com.banana.spring.TomatoApplicationContext;
import com.banana.spring.anno.Component;
import com.banana.spring.web.EmbeddedServer;
import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.io.IOException;
/**
 * @Description: 内嵌tomcat
 * @author zhaojiulin
 * @Date 2025/10/18 13:00
 * @version 1.0
 */
@Component
public class TomcatEmbeddedServer implements EmbeddedServer {
    private TomatoApplicationContext tomatoApplicationContext;
    public TomcatEmbeddedServer(TomatoApplicationContext applicationContext) {
        tomatoApplicationContext = applicationContext;
    }

    private Tomcat tomcat;
    private int port = 7860;

    @Override
    public void start() {
        try {
            tomcat = new Tomcat();

            // 设置端口（避免冲突可以使用8081）
            tomcat.setPort(port);

            // 设置基础目录
            tomcat.setBaseDir(createTempDir().getAbsolutePath());

            // 必须调用getConnector()来初始化连接器
            Connector connector = tomcat.getConnector();
            connector.setProperty("maxThreads", "200");
            connector.setProperty("connectionTimeout", "30000");

            // 3. 创建上下文并添加映射Servlet
            Context ctx = tomcat.addContext("", null);
            Wrapper dispatcher = Tomcat.addServlet(ctx, "dispatcher", new DispatcherServlet(tomatoApplicationContext));
            // 映射所有请求到这个Servlet
            dispatcher.addMapping("/*");
            dispatcher.setLoadOnStartup(1);

            tomcat.start();
            System.out.println("MiniBoot application started on port: " + port);

            tomcat.getServer().await();

        } catch (Exception e) {
            throw new RuntimeException("Failed to start embedded Tomcat", e);
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public int getPort() {
        return 0;
    }

    private static File createTempDir() {
        try {
            File tempDir = File.createTempFile("tomcat.", ".dir");
            tempDir.delete();
            tempDir.mkdir();
            tempDir.deleteOnExit();
            return tempDir;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
