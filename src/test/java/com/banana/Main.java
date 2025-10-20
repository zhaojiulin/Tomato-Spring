package com.banana;

import com.tomato.sprout.TomatoApplicationContext;
import com.tomato.sprout.TomatoBoot;
import com.tomato.sprout.TomatoBootApplication;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

@TomatoBoot
public class Main {
    public static void main(String[] args) {
        TomatoApplicationContext applicationContext = TomatoBootApplication.run(Main.class, args);
        System.out.println(applicationContext.getBean("userService"));
        System.out.println(applicationContext.getBean("contentService"));
        try {
            Enumeration<URL> resources = Main.class.getClassLoader().getResources("resources");
            while (resources.hasMoreElements()) {
                System.out.println(resources.nextElement());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}