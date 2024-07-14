package com.lec.spring.repository;

import com.lec.spring.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

// Repository layer(aka. Data layer)
// DataSource (DB) 등에 대한 직접적인 접근 ★★
public interface PostRepository extends JpaRepository<Post, Long> {

    // TODO :
    //  가급적 기본 JPA 메소드로만 구현해보세요
    //  필요하면 query method 선언해서 사용해도 됨.



}












