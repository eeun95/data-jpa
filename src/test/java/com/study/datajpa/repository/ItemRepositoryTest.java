package com.study.datajpa.repository;

import com.study.datajpa.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired ItemRepository itemRepository;

    @Test
    void save() {
        Item item = new Item();

        // 식별자가 객체일 때 null로 새로운 엔티티인지 구별하는데
        // @GeneratedValue가 아니라 개발자가 직접 아이디를 세팅하는 경우엔
        // persist가 아니라 merge가 됨 (select를 먼저 날리고 데이터가 없으면 save)
        // 저장은 save 수정은 변경감지를 사용해야함 merge는 쓰지 않는 것이 좋다

        // 아이디 임의 생성이 필요할 때는
        // Persistable 인터페이스 사용
        itemRepository.save(item);
    }

}