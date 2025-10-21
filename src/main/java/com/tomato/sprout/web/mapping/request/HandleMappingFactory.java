package com.tomato.sprout.web.mapping.request;

import com.tomato.sprout.constant.RequestMethod;

/**
 * @author zhaojiulin
 * @version 1.0
 * @description: 映射处理工厂类
 * @date 2025/10/21 23:10
 */
public class HandleMappingFactory {

    public static AbstractHandleMapping handleMapping(RequestMethod requestMethod) {
        return switch (requestMethod) {
            case GET -> new GetMappingHandle();
            case POST -> new PostMappingHandle();
            default -> throw new IllegalArgumentException("Unsupported request method: " + requestMethod);
        };

    }
}
