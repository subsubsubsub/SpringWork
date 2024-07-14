package com.lec.spring.di06;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/*
 * stereotype
 *   컨테이너에 bean으로 생성하는 annotation 들
 *
 * @Component       <-- 일반적인 bean
 *  └ ＠Controller   <-- Spring MVC 에서 Controller 로 사용
 *  └ ＠Service      <-- Service 단으로 사용
 *  └ ＠Repository   <-- Repository 단. DAO, Persistence 로 사용
 */

@SpringBootApplication
public class DIMain06 {
    public static void main(String[] args) {
        System.out.println("Main시작");
        SpringApplication.run(DIMain06.class, args);
        System.out.println("Main종료");
    }
}

@Controller
class UserController{
    @Autowired
    private UserService userService;

    public UserController(){
        System.out.println("UserController() 생성");
    }
}

@Service
class UserService{
    @Autowired
    private UserRepository userRepository;

    public UserService(){
        System.out.println("UserService() 생성");
    }
}

@Repository
class UserRepository {

    // DI 설정할때 순환참조(circular reference) 발생하지 않도록 주의!
//    @Autowired  // 순환참조 발생
//    UserController userController;

    public UserRepository() {
        System.out.println("UserRepository() 생성");
    }
}
/*  제어의 역전  // 컨트롤러는 서비스에 의존하고 서비스는 리포지토리에 의존하지만 생성은 역순으로 발생
UserController() 생성
UserService() 생성
UserRepository() 생성
* */