package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    private Long id;    // PK

    @ToString.Exclude   // user 는 toString 에서 제외
    private User user;  // 댓글 작성자(FK)

    @JsonIgnore
    private Long post_id;   // 어느글의 댓글인지    // 게시글의 id

    private String content; // 댓글내용

    // java.time.* 객체 변환을 위한 annotation // 내가 원하는 문자열로 json 문자 변환
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @JsonProperty("regdate")    // 변환하고자 하는 형태 지정 가능
    private LocalDateTime regDate;
}
