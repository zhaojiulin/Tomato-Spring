package com.tomato.sprout.web.mapping.request;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * @author zhaojiulin
 * @version 1.0
 * @description: GET请求
 * @date 2025/10/21 23:09
 */
public class GetMappingHandle extends AbstractHandleMapping{
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    public HashMap<String, Object> doParam(HttpServletRequest req) {
        HashMap<String, Object> paramMap = new HashMap<>();
        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            log.info("parameterName: " + parameterName);
            paramMap.put(parameterName, req.getParameter(parameterName));
        }
        return paramMap;
    }

}
