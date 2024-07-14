package com.lec.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


// Model 객체
// domain 객체
// DTO : Data Transfer Object
// 클라이언트-서버, 서버-db, ...
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private long id;
    private String name;
    private String Subject;
    private String content;
    private LocalDateTime regDate;

    // 웹개발시...
    // 가능한, 다음 3가지는 이름을 일치시켜주는게 좋습니다.
    // 클래스 필드명 = DB 필드명 = form의 name명





}
