package com.study.datajpa.repository;

import com.study.datajpa.dto.MemberDto;
import com.study.datajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

//@Repository를 생략해도됨 (이 어노테이션은 컴포넌트 스캔뿐 아니라 JPA 예외를 스프링 예외로 변환하는 과정도 자동으로 처리)
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 구현체가 없는데 어떻게 기능이 동작할까 ?
    // -> class jdk.proxy2.$Proxy116 스프링이 인터페이스를 보고 프록시 기술로 구현클래스를 만들어 주입해줌
    // JPA가 애플리케이션 로딩 시점에 구현 클래스를 다 만들어준다

    //List<Member> findByUsername(String name);

    // 스프링 데이터 JPA는 메소드 이름을 분석해서 JPQL을 생성하고 실행
    // 길어지면 답이 없음 ,,
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    //@Query(name = "Member.findByUsername")
    // 쿼리 어노테이션을 주석해도 잘 동작하는 이유는
    // -> 1.타입에 있는 엔티티명.메서드명을 가지고 namedQuery를 먼저 찾아서 있으면 실행 2.없으면 메서드명으로 JPQL생성 후 실행
    List<Member> findByUsername(@Param("username") String username);

    // 실무에서 많이 사용
    // 복잡한 JPQL을 바로 넣어 문제 해결 가능
    // @Query = 이름이 없는 namedQuery -> 애플리케이션 로딩 시점에 파싱읋 해서 쿼리를 미리 만들어둠
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new com.study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // 컬렉션 파라미터 바인딩이 되어 in 절을 예쁘게 만들어준다!
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    // 컬렉션, 단건, 옵셔널 .. 반환 타입이 많다
    // 스프링 데이터가 여러 반환 타입을 지원해줌
    List<Member> findListByUsername(String username);
    Member findMemberByUsername(String username);
    Optional<Member> findOptionalByUsername(String username);
}
