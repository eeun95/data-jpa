package com.study.datajpa.repository;

import com.study.datajpa.entity.Member;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

// MemberRepository+Impl 클래스명 규칙을 꼭 지켜주자
// 이 규칙 안지키면 스프링이 빈을 찾지 못함
// 스프링 데이터 JPA가 알아서 조립을 해서 커스텀 메서드를 호출함
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }

    // 쿼리 DSL 쓸 때 커스텀을 많이 사용함

    // 핵심 비즈니스 로직이 아닌 화면에 맞춘 쿼리는 클래스를 따로 분리하는게 나음
    // jpashop 프로젝트 - OrderQueryRepository 처럼
}
