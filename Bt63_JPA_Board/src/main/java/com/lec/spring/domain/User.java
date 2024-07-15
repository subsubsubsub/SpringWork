package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "t8_user")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;  // 회원 아이디

    @Column(nullable = false)
    @JsonIgnore
    private String password;  // 회원 비밀번호

    @Transient  // Entity 에 반영안함.
    @ToString.Exclude   // toString() 에서 제외
    @JsonIgnore
    private String re_password; // 비밀번호 확인 입력 (데이터베이스 저장 X)

    @Column(nullable = false)
    private String name;  // 회원 이름

    @JsonIgnore
    private String email;   // 이메일

    // fetch 기본값
    // @OneToMany, @ManyToMany -> FetchType.Lazy
    // @ManyToOne, @OneToOne -> FetchType.EAGER
    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    @Builder.Default
    @JsonIgnore
    private List<Authority> authorities = new ArrayList<>();

    public void addAuthority(Authority... authority) {
        Collections.addAll(authorities, authority);
    }

    private String provider;
    private String providerId;

}









