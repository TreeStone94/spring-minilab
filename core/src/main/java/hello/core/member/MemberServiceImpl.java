package hello.core.member;

public class MemberServiceImpl implements MemberService {

    // 다형성
    private MemberRepository  memberRepository = new MemoryMemberRepository();

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long id) {
        return memberRepository.findId(id);
    }
}
