package com.study.datajpa.repository;

import com.study.datajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository를 생략해도됨 (이 어노테이션은 컴포넌트 스캔뿐 아니라 JPA 예외를 스프링 예외로 변환하는 과정도 자동으로 처리)
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 구현체가 없는데 어떻게 기능이 동작할까 ?
    // -> class jdk.proxy2.$Proxy116 스프링이 인터페이스를 보고 프록시 기술로 구현클래스를 만들어 주입해줌
    // JPA가 애플리케이션 로딩 시점에 구현 클래스를 다 만들어준다
}
