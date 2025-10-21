package com.banana;

import com.tomato.sprout.TomatoApplicationContext;
import com.tomato.sprout.TomatoBoot;
import com.tomato.sprout.TomatoBootApplication;

@TomatoBoot
public class Main {
    public static void main(String[] args) {
        TomatoApplicationContext applicationContext = TomatoBootApplication.run(Main.class, args);
        System.out.println(applicationContext.getBean("userService"));
        System.out.println(applicationContext.getBean("contentService"));
    }
}