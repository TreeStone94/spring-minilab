package com.example.transaction;

import com.example.transaction.delivery.Delivery;
import com.example.transaction.delivery.DeliveryRepository;
import com.example.transaction.order.Order;
import com.example.transaction.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final DeliveryRepository deliveryRepository;

	// 이 메소드는 하나의 분산 트랜잭션으로 처리됩니다.
   @Transactional
   public String placeOrder(String product, String address) {
       // 1. 주문 정보는 MySQL에 저장
       Order order = new Order();
       order.setProduct(product);
       Order savedOrder = orderRepository.save(order);
       System.out.println("MySQL에 주문 저장 완료: " + savedOrder.getId());

       // 2. 배송 정보는 PostgreSQL에 저장
       Delivery delivery = new Delivery();
       delivery.setOrderId(savedOrder.getId());
       delivery.setAddress(address);
       deliveryRepository.save(delivery);
       System.out.println("PostgreSQL에 배송 정보 저장 완료");

       return "주문 성공! Order ID: " + savedOrder.getId();
   }

   // 롤백 테스트를 위한 메소드
   @Transactional
   public void placeOrderAndFail(String product, String address) {
       // 1. 주문 정보는 MySQL에 저장
       Order order = new Order();
       order.setProduct(product);
       Order savedOrder = orderRepository.save(order);
       System.out.println("MySQL에 주문 저장 시도...");

       // 2. 배송지 주소가 "error"이면 의도적으로 예외 발생
       if ("error".equalsIgnoreCase(address)) {
           throw new RuntimeException("배송지 오류로 인한 강제 롤백!");
       }

       Delivery delivery = new Delivery();
       delivery.setOrderId(savedOrder.getId());
       delivery.setAddress(address);
       deliveryRepository.save(delivery);
       System.out.println("PostgreSQL에 배송 정보 저장 시도...");
   }
}
