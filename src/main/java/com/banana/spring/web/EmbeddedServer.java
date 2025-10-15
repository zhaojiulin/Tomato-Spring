package com.banana.spring.web;

/**
 * 服务
 */
public interface EmbeddedServer {
    void start();
    void stop();
    int getPort();
}
