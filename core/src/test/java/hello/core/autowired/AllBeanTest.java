package hello.core.autowired;

import hello.core.AutoAppConfig;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class AllBeanTest {

	@Test
	public void findAllBean() {
		ApplicationContext ac = new AnnotationConfigApplicationContext (AutoAppConfig.class, DiscountService.class);
		DiscountService discountService = ac.getBean(DiscountService.class);
		Member userA = new Member(1L, "userA", Grade.VIP);
		int discountPrice = discountService.discount(userA, 10000, "fixDiscountPolicy");

		assertThat(discountService).isInstanceOf(DiscountService.class);
		assertThat(discountPrice).isEqualTo(1000);

		int discountPrice2 = discountService.discount(userA, 20000, "rateDiscountPolicy");
		assertThat(discountPrice2).isEqualTo(2000);
	}

	public static class DiscountService {
		private final Map<String, DiscountPolicy> policyMap;
		private final List<DiscountPolicy> policyList;

		public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policyList) {
			this.policyMap = policyMap;
			this.policyList = policyList;
			System.out.println("policyMap = " + policyMap);
			System.out.println("policyList = " + policyList);
		}

		public int discount(Member member, int price, String discountCode) {
			DiscountPolicy discountPolicy = policyMap.get(discountCode);
			int discount = discountPolicy.discount(member, price);
			return discount;
		}
	}
}
