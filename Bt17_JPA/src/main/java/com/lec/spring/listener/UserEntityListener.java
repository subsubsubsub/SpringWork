package com.lec.spring.listener;

import com.lec.spring.domain.User;
import com.lec.spring.domain.UserHistory;
import com.lec.spring.repository.UserHistoryRepository;
import com.lec.spring.support.BeanUtils;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;

// ★ Entity Listener 는 Spring Bean 을 주입 받지 못한다!!
//@Component
public class UserEntityListener {


// ★ Entity Listener 는 Spring Bean 을 주입 받지 못한다!!
//    private final UserHistoryRepository userHistoryRepository;
//
//    // 생성자가 하나만 있는 경우 @Autowired 생략 가능
//    public UserEntityListner(UserHistoryRepository userHistoryRepository) {
//        this.userHistoryRepository = userHistoryRepository;
//    }

    @PostUpdate
    @PostPersist
    public void addUserHistory(Object o) {
        System.out.println(">> UserEntityListener$preUpdate()");

        // 스프링 bean 객체 주입 받기
        UserHistoryRepository userHistoryRepository = BeanUtils.getBean(UserHistoryRepository.class);

        User user = (User) o;
        // UserHistory 에 UPDATE 될 User 정보를 담아서 저장 (INSERT)
        UserHistory userHistory = new UserHistory();
//        userHistory.setUserId(user.getId());
        userHistory.setName(user.getName());
        userHistory.setEmail(user.getEmail());
        userHistory.setUser(user);

        userHistoryRepository.save(userHistory);    // INSERT
    }
}
