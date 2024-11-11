package hello.core.memeber;

import hello.core.AppConfig;
import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MemberServiceTest {


    MemberService memberService;

    // 테스트 실행전에 실행되는 메소드
    @BeforeEach
    public void beforeEach() {
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
    }
//    MemberService memberService = new MemberServiceImpl();

    @Test
    void join() {
        // given
        Member memeber = new Member(1L, "홍길동", Grade.VIP);

        // when
        memberService.join(memeber);
        Member findMember = memberService.findMember(1L);

        // then
        Assertions.assertEquals(memeber, findMember); // member, findMember 같은지 검증
    }
}
