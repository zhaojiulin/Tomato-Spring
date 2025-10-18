package com.banana;

import com.banana.spring.TomatoApplicationContext;
import com.banana.spring.TomatoBoot;
import com.banana.spring.TomatoBootApplication;

@TomatoBoot
public class Main {
    public static void main(String[] args) {
        TomatoApplicationContext applicationContext = TomatoBootApplication.run(Main.class, args);
        System.out.println(applicationContext.getBean("userService"));
        System.out.println(applicationContext.getBean("contentService"));
    }
}