package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String username;  // 회원 아이디
    @JsonIgnore
    private String password;  // 회원 비밀번호

    @ToString.Exclude   // toString() 에서 제외
    @JsonIgnore
    private String re_password; // 비밀번호 확인 입력 (데이터베이스 저장 X)

    private String name;  // 회원 이름
    @JsonIgnore
    private String email;   // 이메일

    private LocalDateTime regDate;

    private String provider;
    private String providerId;

}









