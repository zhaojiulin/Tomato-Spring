package com.banana.spring.web;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface HandlerMapping {
    HandlerMethod getHandler(HttpServletRequest request);
    void registerHandler(String url, HandlerMethod handlerMethod);
    Map<String, HandlerMethod> getHandlerMap();
}
