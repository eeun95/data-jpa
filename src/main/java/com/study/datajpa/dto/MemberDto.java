package com.study.datajpa.dto;

import com.study.datajpa.entity.Member;
import lombok.Data;

@Data
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    // DTO는 엔티티를 봐도 됨
    // 엔티티는 가급적이면 DTO를 바라보면 안됨..(같은 패키지면 상관없지만)
    public MemberDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
    }
}
