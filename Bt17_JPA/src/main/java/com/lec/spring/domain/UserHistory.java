package com.lec.spring.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// User 의 히스토리 정보 저장
// '수정하기 전의 데이터' 가 아니라
// '수정될 내용' 을 History 에 담는 예제.
@Data
@NoArgsConstructor
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
//@EntityListeners(value= AuditingEntityListener.class)
public class UserHistory extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // UserHistory 의 id

//    @Column(name = "user_id", insertable = false, updatable = false)   // User.java 에 @JoinColumn 과 연동
//    private Long userId;    // User 의 id
    private String name;    // User 의 name
    private String email;   // User 의 email

    @ManyToOne
//    @ToString.Exclude   // 순환참조 방지
    private User user;  // user_id

//    @CreatedDate
//    private LocalDateTime createAt;
//    @LastModifiedDate
//    private LocalDateTime updateAt;
}
