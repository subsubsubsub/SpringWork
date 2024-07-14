package com.lec.spring.service;

import com.lec.spring.domain.Comment;
import com.lec.spring.domain.QryCommentList;
import com.lec.spring.domain.QryResult;
import com.lec.spring.domain.User;
import com.lec.spring.repository.CommentRepository;
import com.lec.spring.repository.UserRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    private UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(SqlSession sqlSession) {
        commentRepository = sqlSession.getMapper(CommentRepository.class);
        userRepository = sqlSession.getMapper(UserRepository.class);
    }

    @Override
    public QryCommentList list(Long postId) {
        QryCommentList list = new QryCommentList();

        List<Comment> comments = commentRepository.findByPost(postId);  // 특정글의 댓글 쫙 가져왔어요

        list.setCount(comments.size());
        list.setList(comments);
        list.setStatus("OK");

        return list;
    }

    @Override
    public QryResult write(Long postId, Long userId, String content) {  // 특정 글에 특정 사용자가 댓글을 작성
        User user = userRepository.findById(userId);

        Comment comment = Comment.builder()
                .user(user)
                .content(content)
                .post_id(postId)
                .build();

        int cnt = commentRepository.save(comment);

        QryResult result = QryResult.builder()
                .count(cnt)
                .status("OK")
                .build();

        return result;
    }

    @Override
    public QryResult delete(Long id) {

        int cnt = commentRepository.deleteById(id); // 정상 삭제된 경우 1 리턴
        String status = "FAIL";
        if (cnt > 0) status = "OK";

        QryResult result = QryResult.builder()
                .count(cnt)
                .status(status)
                .build();


        return result;
    }
}
