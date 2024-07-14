package com.lec.spring.repository;

import com.lec.spring.domain.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {

    // 특정 userId 의 UserHistory 가져오기
    List<UserHistory> findByUserId(Long userId);
}
