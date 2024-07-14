package com.lec.spring.listener;


// 여러 Entity 에서 동일하게 사용될 Listener 들을 담은 클래스를 준비해보자.

// @EntityListeners 로 Entity 에 지정이 되면
// Entity 가 아닌객체임에도
// @PrePersist, @PreUpdate 지정가능

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class MyEntityListener {

    @PrePersist
    public void prePersist(Object o) {  // 반드시 Object 매개변수 필요!
        System.out.println(">> MyEntityListener#prePersist() 호출");
        if (o instanceof Auditable) {
            ((Auditable) o).setCreatedAt(LocalDateTime.now());
            ((Auditable) o).setUpdatedAt(LocalDateTime.now());
        }

    }

    @PreUpdate
    public void preUpdate(Object o) {
        System.out.println(">> MyEntityListener#preUpdate() 호출");

        if (o instanceof Auditable) {
            ((Auditable) o).setUpdatedAt(LocalDateTime.now());
        }
    }
}
