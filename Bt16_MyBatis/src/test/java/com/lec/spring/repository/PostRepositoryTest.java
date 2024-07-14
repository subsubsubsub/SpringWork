package com.lec.spring.repository;

import com.lec.spring.domain.Post;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest  // 스프링 context 를 로딩하여 테스트에 사용
class PostRepositoryTest {

    // MyBatis 가 생성한 구현체들이 담겨 있는 SqlSession 객체
    // 기본적으로 스프링에 빈 생성되어있어서 주입 받을수 있다

    @Autowired  // 주입 받아서 사용
    private SqlSession sqlSession;

    @Test
    void test0() {
        PostRepository postRepository = sqlSession.getMapper(PostRepository.class);
        // sqlSession 에서 안에 있는 PostRepository 를 리턴

        System.out.println("[최초]");
        postRepository.findAll().forEach(System.out::println);

        // 저장하기 위한 데이터 입력
        Post post = Post.builder()  // 초기화 하고 싶은 필드만 체이닝 해서 만들 수 있다.
                .user("김다현")
                .subject("늦게 일어났어요")
                .content("효효효효효")
                .build();

        System.out.println("[생성전] " + post);    // save 를 호출하기 전 id 값은 null

        // 생성한 데이터 (post) 데이터베이스에 저장
        int result = postRepository.save(post); // 정수 리턴

        System.out.println("[생성후] " + post);
        System.out.println("save() 결과:" + result);

        System.out.println("[신규 생성후]");
        postRepository.findAll().forEach(System.out::println);

        Long id = post.getId(); // 생성 된 id 값 리턴
        for (int i = 0; i < 5; i++) {   // 조회수 5회 증가
            postRepository.incViewCnt(id);
        }
        post = postRepository.findById(id);
        System.out.println("[조회수 증가 후] " + post);

        // content, subject 내용 수정
        post.setContent("감기 조심하세요");
        post.setSubject("에러 메세지 보세요");

        // 수정한 내용 업데이트
        postRepository.update(post);
        post = postRepository.findById(id);
        System.out.println("[수정후] " + post);


        postRepository.delete(post);
        System.out.println("[삭제후]");
        postRepository.findAll().forEach(System.out::println);
    }

}








