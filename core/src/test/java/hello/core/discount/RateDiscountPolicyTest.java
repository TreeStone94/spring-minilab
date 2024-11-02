package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class RateDiscountPolicyTest {

    DiscountPolicy discountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("VIP는 가격에 10%할인이 되어야 한다")
    void discount_o() {
        Member member = new Member(1L, "홍길동", Grade.VIP);

        assertEquals(discountPolicy.discount(member, 20000), 2000);
    }

    @Test
    @DisplayName("BASIC는 가격에 0%할인이 되어야 한다")
    void discount_x() {
        Member member = new Member(2L, "김철수", Grade.Basic);

        assertEquals(discountPolicy.discount(member, 20000), 0);
    }
}