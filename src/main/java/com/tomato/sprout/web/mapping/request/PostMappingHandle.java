package com.tomato.sprout.web.mapping.request;

import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author zhaojiulin
 * @version 1.0
 * @description: POST请求
 * @date 2025/10/21 23:13
 */
public class PostMappingHandle extends AbstractHandleMapping {
    @Override
    public HashMap<String, Object> doParam(HttpServletRequest request) {
        HashMap<String, Object> paramMap = new HashMap<>();
        // 1. 获取请求体输入流
        try {
            BufferedReader reader = request.getReader();
            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            String jsonBody = jsonBuilder.toString();
            System.out.println("原始JSON: " + jsonBody);
            // 暂支持单个
            paramMap.put("arg0", jsonBody);
            return paramMap;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
