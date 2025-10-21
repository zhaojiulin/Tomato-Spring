package com.tomato.sprout.web.mapping.request;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;

/**
 * @author zhaojiulin
 * @version 1.0
 * @description: RequestMapping请求处理参数
 * @date 2025/10/21 23:07
 */
public interface HandleMapping {
    HashMap<String, Object> doParam(HttpServletRequest req);
}
