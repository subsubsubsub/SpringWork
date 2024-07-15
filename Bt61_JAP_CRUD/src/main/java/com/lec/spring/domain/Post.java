package com.lec.spring.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder    // builder pattern 사용 가능
@Entity(name = "TBL_POST")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)   // ← nullable=false 는 ddl-auto: update 에선 동작하지 않으므로 create 나 create-drop 으로 적용해야 한다.
    private String user;    // 작성자

    @Column(nullable = false)
    private String subject; // 글 제목

    @Column(
            columnDefinition = "LONGTEXT"   // <- MySQL, Postgre 의 경우 사용가능.
//            , length = 1000     // Oracle 의 경우 (varchar2(1000) 으로 지정됨)
    )
    private String content; // 내용
    private LocalDateTime regDate;  // 작성일

    @ColumnDefault(value = "0")
    @Column(insertable = false)
    private Long viewCnt;   // 조회수  // primitive (long) 으로 설정하면 @Column(insertable = false) 없이도 자동으로 not null 로 설정된다.

    @PrePersist
    private void prePersist() {
        this.regDate = LocalDateTime.now();
    }

}
