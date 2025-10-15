package com.banana.test;

import com.banana.spring.anno.Autowired;
import com.banana.spring.anno.Component;

@Component("userService")
public class UserService {
    @Autowired
    private OrderService orderService;
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public void test() {
        System.out.println(name);
        orderService.order();
    }
}
