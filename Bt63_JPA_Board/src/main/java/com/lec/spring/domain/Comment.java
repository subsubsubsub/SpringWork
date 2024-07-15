package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.BindParam;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity(name = "t8_comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // PK

    @Column(nullable = false)
    private String content; // 댓글내용

    // Comment:User = N:1   특정 댓글의 -> 작성자 정보 필요.
    @ManyToOne(optional = false)
    @ToString.Exclude   // user 는 toString 에서 제외
    private User user;  // 댓글 작성자(FK)

    @Column(name = "post_id")
    @JsonIgnore
    private Long post;   // 어느글의 댓글인지    // 게시글의 id



}
