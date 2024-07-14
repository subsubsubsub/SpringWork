package com.lec.spring.service;


import com.lec.spring.domain.Post;
import com.lec.spring.repository.PostRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

// 서비스와 리포지토리는 인터페이스를 먼저 만드는 것이 좋다

@Service
public class BoardServiceImpl implements BoardService {

    PostRepository postRepository;

    public BoardServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public int write(Post post) {

        int result = 0;

        if (post != null) {
            postRepository.save(post);
            result = 1;
        }
        return result;
    }

    @Override
    @Transactional
    public Post detail(Long id) {

        Post post = postRepository.findById(id).orElse(null);

        if (post != null) {
            post.setViewCnt(post.getViewCnt() + 1);
            postRepository.saveAndFlush(post);
        }

        return post;
    }

    @Override
    public List<Post> list() {
        return postRepository.findAll(Sort.by(Sort.Order.desc("id")));
    }

    @Override
    public Post selectById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public int update(Post post) {
        Post data = postRepository.findById(post.getId()).orElse(null);
        if (data == null) return 0;

        data.setSubject(post.getSubject());
        data.setContent(post.getContent());

        postRepository.save(data);
        return 1;
    }
//    @Override
//    public int update(Post post) {
//
//        int result = 0;
//
//        if (post != null) {
//            postRepository.save(post);
//            result = 1;
//        }
//        return result;
//    }

    @Override
    public int deleteById(Long id) {

        int result = 0;

        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);  // SELECT + DELETE
            result = 1;
        }

        return result;
    }
}   //end Service
