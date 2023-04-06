package com.study.datajpa.repository;

import com.study.datajpa.dto.MemberDto;
import com.study.datajpa.entity.Member;
import com.study.datajpa.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Test
    void testMember() {
        System.out.println("memberRepository = " + memberRepository.getClass());
        Member member = new Member("memberA");
        Member saveMember = memberRepository.save(member);
        Optional<Member> findMember = memberRepository.findById(saveMember.getId());

        Assertions.assertThat(saveMember.getId()).isEqualTo(findMember.get().getId());
        Assertions.assertThat(saveMember).isEqualTo(findMember.get());
    }

    @Test
    void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        // 단건 조회 검증
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deleteCount = memberRepository.count();
        Assertions.assertThat(deleteCount).isEqualTo(0);

    }

    @Test
    void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void testNamedQuery() {
        Member m1 = new Member("member1", 10);
        Member m2 = new Member("member2", 10);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("member1");
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("member1");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(10);
        Assertions.assertThat(result.size()).isEqualTo(1);

    }

    @Test
    void testQuery() {
        Member m1 = new Member("member1", 10);
        Member m2 = new Member("member2", 10);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("member1", 10);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("member1");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(10);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void findUsernameList() {
        Member m1 = new Member("member1", 10);
        Member m2 = new Member("member2", 10);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> result = memberRepository.findUsernameList();
        for (String s : result) {
            System.out.println("name = "+s);
        }
    }

    @Test
    void findMemberDto() {

        Team team = new Team("team1");
        teamRepository.save(team);

        Member m1 = new Member("member1", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = "+dto);
        }
    }
    @Test
    void findByNames() {

        Member m1 = new Member("member1", 10);
        Member m2 = new Member("member2", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> members = memberRepository.findByNames(Arrays.asList("member1", "member2"));
        for (Member m : members) {
            System.out.println("member = "+m);
        }
    }
    @Test
    void returnType() {

        Member m1 = new Member("member1", 10);
        Member m2 = new Member("member2", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        // 컬렉션은 없는 결과일 경우 빈 컬렉션을 반환(!= null)
        List<Member> member1 = memberRepository.findListByUsername("member1");
        // 단건 조회가 없는 결과일 경우 null
        Member member2 = memberRepository.findMemberByUsername("member1");
        Optional<Member> member3 = memberRepository.findOptionalByUsername("member1");

        System.out.println(member1.get(0).getUsername()+" "+member2.getUsername()+" "+member3.get().getUsername());
    }

    @Test
    void paging() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;

        // 구현체
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "username");

        // 스프링 데이타 JPA가 반환타입이 Page인 것을 보고 카운트 쿼리를 날림
        // Slice 는 전체 count를 날리지 않고 +1개만 요청

        Slice<Member> page = memberRepository.findByAge(age, pageRequest);
        // DTO로 변환해서 넘겨야함 그대로 반환하면 혼난다
        // 컨텐츠가 json으로 반환됨 편리!
        Slice<MemberDto> memberDtos = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

        List<Member> content = page.getContent();
        for (Member member : content) {
            System.out.println("member = "+member);
        }
        // totalElement == totalCount
        //System.out.println(page.getTotalElements());

        Assertions.assertThat(content.size()).isEqualTo(3);
        //Assertions.assertThat(page.getTotalElements()).isEqualTo(4);
        Assertions.assertThat(page.getNumber()).isEqualTo(0);
        //Assertions.assertThat(page.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.hasNext()).isTrue();

    }
}