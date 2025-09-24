package com.example.transaction;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order/success")
    public String orderSuccess() {
        return orderService.placeOrder("MacBook Pro M4", "Seoul, Korea");
    }

    @PostMapping("/order/fail")
    public String orderFail() {
        try {
            orderService.placeOrderAndFail("iPhone 17", "error");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "주문 실패! 트랜잭션이 롤백되었습니다.";
        }
        return "예외가 발생하지 않아 테스트 실패";
    }
}
