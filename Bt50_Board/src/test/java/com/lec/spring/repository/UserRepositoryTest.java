package com.lec.spring.repository;

import com.lec.spring.domain.User;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private SqlSession sqlSession;

    @Test
    void test1() {
        UserRepository userRepository = sqlSession.getMapper(UserRepository.class);
        AuthorityRepository authorityRepository = sqlSession.getMapper(AuthorityRepository.class);

        User user = userRepository.findById(3L);
        System.out.println("findById(): " + user);
        var list = authorityRepository.findByUser(user);
        System.out.println("권한들: " + list);

        user = User.builder()
                .username("USER4")
                .password(encoder.encode("1234"))
                .name("회원3")
                .email("user3@mail.com")
                .build();

        System.out.println("save() 전: " + user);
        userRepository.save(user);
        System.out.println("save() 후: " + user);

        authorityRepository.addAuthority(user.getId(), authorityRepository.findByName("ROLE_MEMBER").getId());
        System.out.println("권한들: " + authorityRepository.findByUser(user));
    }

    @Autowired
    PasswordEncoder encoder;

    @Test
    void passwordEncoderTest(){
        String rawPassword = "1234";

        for (int i = 0; i < 10; i++) {
            System.out.println(encoder.encode(rawPassword));    // rawPassword 를 암호화 => 실행 할 때마다 다르다
        }
    }

}