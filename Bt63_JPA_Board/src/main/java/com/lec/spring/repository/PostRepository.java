package com.lec.spring.repository;

import com.lec.spring.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository layer(aka. Data layer)
// DataSource (DB) 등에 대한 직접적인 접근 ★★
public interface PostRepository extends JpaRepository<Post, Long> {
}












