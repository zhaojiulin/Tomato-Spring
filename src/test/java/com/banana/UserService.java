package com.banana;

import com.tomato.sprout.anno.Autowired;
import com.tomato.sprout.anno.Component;

@Component("userService")
public class UserService {
    @Autowired
    private OrderService orderService;
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public void test() {
        System.out.println("user test" + name);
        orderService.order();
    }

    public String testReturn(String title) {
        return "user testReturn" + title;
    }
}
