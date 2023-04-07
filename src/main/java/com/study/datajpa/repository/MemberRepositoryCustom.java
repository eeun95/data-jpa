package com.study.datajpa.repository;

import com.study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    /*
    스프링 데이터 JPA 리포지토리는 인터페이스만 정의하고 구현체는 스프링이 자동 생성
    인터페이스의 메서드를 직접 구현하고 싶다면?
    1. JPA 직접 사용
    2. 스프링 JDBC Template 사용
    3. MyBatis 사용
    4. 데이터베이스 커넥션 직접 사용
    5. QueryDSL 사용
    */

    List<Member> findMemberCustom();
}
