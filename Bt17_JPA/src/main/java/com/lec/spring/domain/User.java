package com.lec.spring.domain;

import com.lec.spring.listener.UserEntityListener;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor    // NonNull 어노테이션이 붙은 변수를 이용한 생성자 하나 더 추가
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity  // 반드시 @Id 등 pk 지정 해줘야 함  // Entity 객체 지정. 이 객체가 JPA 에서 관리하는 Entity 객체임을 알림
@Table(name = "T_USER"    // DB 테이블명
        , indexes = {@Index(columnList = "name")}  // 컬럼에 댕한 index 생성
        , uniqueConstraints = {@UniqueConstraint(columnNames = {"email", "name"})}  // unique 제약사항
)
@EntityListeners(value = {UserEntityListener.class})
public class User extends BaseEntity {

    @Id // PK 지정    필수요소
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 마치  MySQL 의 autoIncrement 와 같은 동작 수행, 타겟 데이터 베이스에 따라 달라지게 할 수 있다.(오라클, 마리아...)
    private Long id;

    @NonNull
    private String name;
    @NonNull
    @Column(unique = true)
    private String email;

    //    @Column(
//            name = "crtdat",
//            nullable = false)
//    @Column(updatable = false)  // UPDATE 동작시 해당 컬럼은 생략.
//    @CreatedDate    // AuditingEntityListener 가 Listener 로 적용시 사용
//    private LocalDateTime createdAt;    // created_at

    //    @Column(insertable = false) // INSERT 동작시 해당 컬럼은 생략.
//    @LastModifiedDate   // AuditingEntityListener 가 Listener 로 적용시 사용
//    private LocalDateTime updatedAt;

    // User:Address => 1:N
//    @OneToMany(fetch = FetchType.EAGER) // FetchType.EAGER => 유저가 로딩될때 address 도 같이 로딩해라
//    private List<Address> address;

    @Transient  // jakarta.persistence      DB 에 반영 안하는 필드 속성. 영속성 부여 안함.
    private String testData;    // test_data

    @Enumerated(value = EnumType.STRING)    // 데이터베이스 상에도 문자열로 저장되게 한다.
    private Gender gender;

    // User : UserHistory => 1:N 관계
    @OneToMany(fetch = FetchType.EAGER) // 즉시로딩, Entity 를 로드할 때 연관된 엔티티도 함께 로드한다.
    @JoinColumn(name = "user_id" // Entity 가 어떤 컬럼으로 join 하게 될지 지정해준다.
            // name = "user_id"   : join 할 컬럼명 지정가능!
            //          UserHistory 의 user_id 란 컬럼으로 join
            , insertable = false, updatable = false
            // User 에서 userHistories 값을 추가, 수정하지 못하도록 하기 위해
    )
    @ToString.Exclude
    private List<UserHistory> userHistories = new ArrayList<>();        // NPE 방지

    // User:Review = 1:N
    @OneToMany
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private List<Review> reviews = new ArrayList<>();


//    @PrePersist // INSERT 직전에 실행
//    public void prePersist() {
//        System.out.println(">> prePersist");
//        // 매번 INSERT 가 동작하기 전에 동작함으로 반복되는 작업을 최소화 할 수 있다.
//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
//    }

//    @PreUpdate
//    public void preUpdate() {
//        System.out.println(">>> preUpdate");
//        this.updatedAt = LocalDateTime.now();
//    }
//
//    @PreRemove
//    public void preRemove() {
//        System.out.println(">>> preRemove");
//    }
//
//    @PostPersist
//    public void postPersist() {
//        System.out.println(">>> postPersist");
//    }
//
//    @PostUpdate
//    public void postUpdate() {
//        System.out.println(">>> postUpdate");
//    }
//
//    @PostRemove
//    public void postRemove() {
//        System.out.println(">>> postRemove");
//    }
//
//    @PostLoad
//    public void postLoad() {
//        System.out.println(">>> postLoad");
//    }
//


}
