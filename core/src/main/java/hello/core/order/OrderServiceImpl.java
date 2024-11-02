package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member findMember = memberRepository.findId(memberId);
        // 설계의 중요성: 할인에 대한건 할인정책 서비스에서 알아서 처리해줘(단일 책임 원칙)
        int discountPrice = discountPolicy.discount(findMember, itemPrice);
        memberRepository.findId(memberId);
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
