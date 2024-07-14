package com.lec.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder    // builder pattern 사용 가능
public class Post {
    private Long id;
    private String user;    // 작성자
    private String subject; // 글 제목
    private String content; // 내용
    private LocalDateTime regDate;  // 작성일
    private Long viewCnt;   // 조회수
}
