package com.banana.spring.web.servlet;

import com.banana.spring.TomatoApplicationContext;
import com.banana.spring.web.mapping.HandlerMethod;
import com.banana.spring.web.mapping.MethodInvoker;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * @author zhaojiulin
 * @version 1.0
 * @Description: 前端控制器
 * 负责处理HTTP请求处理
 * 参数解析
 * 响应数据转换
 * @Date 2025/10/18 12:56
 */
public class DispatcherServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(DispatcherServlet.class.getName());
    private final TomatoApplicationContext applicationContext;

    /**
     * @description: 获取上下文
     * @author zhaojiulin
     * @param: null
     * @return:
     * @Date: 2025/10/18 12:57
     */
    public DispatcherServlet(TomatoApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * @description: 请求处理
     * @author zhaojiulin
     * @param: null
     * @return:
     * @Date: 2025/10/18 12:57
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        doDispatch(req, resp);
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) {
        String reqMethod = req.getMethod();
        String requestURI = req.getRequestURI();
        log.info("requestURI: " + requestURI);
        log.info("method: " + reqMethod);
        HandlerMethod handlerMethod = applicationContext.getHandlerMethod(reqMethod.toUpperCase(Locale.ROOT), requestURI);
        if (Objects.isNull(handlerMethod)) {
            sendJsonResponse(resp, 500, "{\"error\": \"接口未找到: " + reqMethod + " " + requestURI + "\"}");
            return;
        }
        HashMap<String, Object> paramMap = doParamMap(req);
        MethodInvoker methodInvoker = new MethodInvoker();
        try {
            Object object = methodInvoker.invokeHandler(handlerMethod, paramMap);
            sendJsonResponse(resp, 200, Objects.nonNull(object) ? object.toString() : "");
        } catch (InvocationTargetException e) {
            sendJsonResponse(resp, 500, "{\"error\": \"Invocation ERROR: " + reqMethod + " " + requestURI + "\"}");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @description: 请求参数解析
     * @author zhaojiulin
     * @param: req http请求
     * @return: 参数map集合
     * @Date: 2025/10/18 12:58
     */
    private HashMap<String, Object> doParamMap(HttpServletRequest req) {
        HashMap<String, Object> paramMap = new HashMap<>();
        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            log.info("parameterName: " + parameterName);
            paramMap.put(parameterName, req.getParameter(parameterName));
        }
        return paramMap;
    }


    private void sendJsonResponse(HttpServletResponse resp, int status, String json) {
        resp.setStatus(status);
        resp.setContentType("application/json;charset=utf-8");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = null;
        try {
            out = resp.getWriter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!json.isEmpty()) {
            out.print(json);
        }
        out.flush();
    }

}
