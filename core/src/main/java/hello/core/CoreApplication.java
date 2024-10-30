package hello.core;

import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CoreApplication {

	public static void main(String[] args) {

		Member member = new Member(1L, "홍길동", "BASIC");

		MemberService memberService = new MemberServiceImpl();

		memberService.join(member);

		System.out.println(memberService.findMember(1L).getName());
		SpringApplication.run(CoreApplication.class, args);
	}

}
