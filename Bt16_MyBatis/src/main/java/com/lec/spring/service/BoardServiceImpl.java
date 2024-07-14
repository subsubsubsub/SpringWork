package com.lec.spring.service;

import com.lec.spring.domain.Post;
import com.lec.spring.repository.PostRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 서비스와 리포지토리는 인터페이스를 먼저 만드는 것이 좋다

@Service
public class BoardServiceImpl implements BoardService {
    private PostRepository postRepository;

    // SqlSession sqlSession : MyBatis의 sqlSession 객체, SQL 실행, 매퍼 인터페이스의 메서드 호출 등을 처리한다.
    @Autowired
    public BoardServiceImpl(SqlSession sqlSession){  // MyBatis 가 생성한 SqlSession 빈(bean) 객체 주입
        postRepository = sqlSession.getMapper(PostRepository.class);
        System.out.println("BoardService() 생성");
    }

    @Override
    public int write(Post post) {
        return postRepository.save(post);   // Post 객체를 데이터베이스에 저장
    }

    @Override
    @Transactional
    public Post detail(Long id) {
        // 게시물 상세보기 로직
        postRepository.incViewCnt(id);  // 조회수 증가
        Post post = postRepository.findById(id);    // 읽어오기
        return post;
    }

    @Override
    public List<Post> list() {
        // 게시물 목록 조회 로직
        return postRepository.findAll();
    }

    @Override
    public Post selectById(Long id) {
        // 특정 id 의 게시물 조회 로직
        Post post = postRepository.findById(id);
        return post;
    }

    @Override
    public int update(Post post) {
        // 게시물 수정
        return postRepository.update(post);
    }

    @Override
    public int deleteById(Long id) {

        // 게시물 삭제
        int result = 0;

        Post post = postRepository.findById(id);    // 존재하는 데이터인지 확인
        if (post != null) { // 존재 한다면 삭제진행
            result = postRepository.delete(post);
        }

        return result;
    }
}   //end Service
