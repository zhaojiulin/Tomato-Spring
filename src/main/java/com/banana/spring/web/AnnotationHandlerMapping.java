package com.banana.spring.web;

import com.banana.spring.TomatoApplicationContext;
import com.banana.spring.handle.ApplicationContextAware;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private TomatoApplicationContext applicationContext;

    public void initMapping(){
        
    }

    @Override
    public HandlerMethod getHandler(HttpServletRequest request) {



        return null;
    }

    @Override
    public void registerHandler(String url, HandlerMethod handlerMethod) {

    }

    @Override
    public Map<String, HandlerMethod> getHandlerMap() {
        return Map.of();
    }
}
