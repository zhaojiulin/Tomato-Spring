package com.banana;

import com.banana.spring.anno.Component;

@Component
public class OrderService {
    public void order() {
        System.out.println("Order Service");
    }
}
