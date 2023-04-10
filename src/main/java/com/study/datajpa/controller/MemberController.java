package com.study.datajpa.controller;

import com.study.datajpa.entity.Member;
import com.study.datajpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        // 스프링부트가 자동으로 컨버터 해서 Member 객체를 반환해준다
        // 조회용으로만 활용해야 한다. 트랜잭션이 없는 범위에서 조회했으므로 엔티티를 변경해도 DB반영 X
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<Member> list(@PageableDefault(size=5) Pageable pageable) {
        // 글로벌 설정보다 우선권을 가짐
        Page<Member> page = memberRepository.findAll(pageable);
        return page;

        // 페이징 정보가 둘 이상이면 접두사로 구분
        /*
         * @Qualifier("member") Pageable memberPageable,
         * @Qualifier("order") Pageable orderPageable,
         *
         * localhost:8080/membersmember_page=0&order_page=1 이런식으로..
        * */
    }

    @PostConstruct
    public void init() {
        for(int i=0; i<100; i++) {
            memberRepository.save(new Member("User"+i, i));
        }
    }
}
