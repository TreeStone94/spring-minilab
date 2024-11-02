package hello.core.order;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderServiceTest {

    @Test
    void createOrder() {
        MemberService memberService = new MemberServiceImpl();
        OrderService orderService = new OrderServiceImpl();

        Member member = new Member(1L, "홍길동", Grade.VIP);
        memberService.join(member);

        Order order =  orderService.createOrder(member.getId(), "자전거", 10000);

        Assertions.assertEquals(order.getDiscountPrice(), 1000);
    }
}
