package com.lec.spring.di03;

import com.lec.spring.beans.Score;
import com.lec.spring.beans.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DIMain03 implements CommandLineRunner {

    @Autowired  // 자동주입. 필드타입과 같은 bean 이 컨테이너 안에 있으면 자동 주입 받는다.
    Score scoreA;

    @Autowired
    Student stu1;

    @Autowired
    ApplicationContext ctx;

    public DIMain03(){
        System.out.println("DIMain03() 생성");
    }

    public static void main(String[] args) {
        System.out.println("Main 시작");
        SpringApplication.run(DIMain03.class, args);
        System.out.println("Main 종료");
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(scoreA);
        System.out.println(stu1);

        System.out.println(scoreA == stu1.getScore());  // true 둘은 같은 객체다!

        // 컨테이너에 생성된 bean은 'bean 의 이름' 으로
        // bean 객체를 받아올수 있다.
        System.out.println(ctx.getBean("score1"));
        System.out.println(ctx.getBean("stu1"));
    }
}
