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
        TomatoApplicationContext tomatoApplicationContext = TomatoBootApplication.startContext(Main.class, args);
        System.out.println(tomatoApplicationContext.getBean("userService"));
        System.out.println(tomatoApplicationContext.getBean("contentService"));
    }
}