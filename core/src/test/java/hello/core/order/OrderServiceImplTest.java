package hello.core.order;

import hello.core.discount.FixDiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {

	@Test
	void creatOrder() {
		MemoryMemberRepository memoryMemberRepository = new MemoryMemberRepository();
		memoryMemberRepository.save(new Member(1L,"홍길동", Grade.VIP));
		OrderServiceImpl orderService = new OrderServiceImpl(memoryMemberRepository, new FixDiscountPolicy());
		Order itemA = orderService.createOrder(1L, "itemA", 10000);
		Assertions.assertThat(itemA.getDiscountPrice()).isEqualTo(1000);
	}
}