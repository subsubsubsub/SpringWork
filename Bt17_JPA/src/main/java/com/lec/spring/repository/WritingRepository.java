package com.lec.spring.repository;

import com.lec.spring.domain.Writing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WritingRepository extends JpaRepository<Writing, Long> {
}
