package com.banana.spring.web.servlet;

import com.banana.spring.anno.Component;
import com.banana.spring.web.EmbeddedServer;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

@Component
public class TomcatEmbeddedServer implements EmbeddedServer {
    private Tomcat tomcat;
    private int port = 8080;
    @Override
    public void start() {
        try {
            tomcat = new Tomcat();
            tomcat.setPort(port);

            String contextPath = "";
            String docBase = new File(".").getAbsolutePath();

            Context context = tomcat.addContext(contextPath, docBase);

            // 添加DispatcherServlet
            Tomcat.addServlet(context, "dispatcher", new DispatcherServlet());
            context.addServletMappingDecoded("/*", "dispatcher");

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
}
