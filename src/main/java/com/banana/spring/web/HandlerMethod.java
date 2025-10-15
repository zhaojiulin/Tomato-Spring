package com.banana.spring.web;

import com.banana.spring.constant.RequestMethod;

import java.lang.reflect.Method;
import java.util.Map;

public class HandlerMethod {
    private Object controller;     // 控制器实例
    private Method method;         // 处理方法
    private String url;            // 请求URL
    private RequestMethod[] httpMethods; // HTTP方法
    private boolean responseBody;  // 是否返回JSON

    // 参数信息：参数名 -> 参数类型
    private Map<String, Class<?>> parameters;

    public HandlerMethod(Object controller, Method method, String url,
                         RequestMethod[] httpMethods, Map<String, Class<?>> parameters) {
        this.controller = controller;
        this.method = method;
        this.url = url;
        this.httpMethods = httpMethods;
        this.parameters = parameters;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RequestMethod[] getHttpMethods() {
        return httpMethods;
    }

    public void setHttpMethods(RequestMethod[] httpMethods) {
        this.httpMethods = httpMethods;
    }

    public boolean isResponseBody() {
        return responseBody;
    }

    public void setResponseBody(boolean responseBody) {
        this.responseBody = responseBody;
    }

    public Map<String, Class<?>> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Class<?>> parameters) {
        this.parameters = parameters;
    }
}
