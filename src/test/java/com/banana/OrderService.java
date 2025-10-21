package com.banana;

import com.tomato.sprout.anno.Autowired;
import com.tomato.sprout.anno.Component;

@Component
public class OrderService {
    public void order() {
        System.out.println("Order Service");
    }
}
