package com.lec.spring.di05;

import com.lec.spring.beans.MessageBean;
import com.lec.spring.beans.Student;
import com.lec.spring.di04.DIMain04;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * Autowired (자동주입) 는 세군데 발생
 * - 생성자  (constructor injection)
 * - 멤버변수 (field injection)
 * - setter (setter injection)
 */


@SpringBootApplication
public class DIMain05 implements CommandLineRunner {

    Student stu1;
    Student stu2;

    @Autowired // field injection
    MessageBean msg1;

    MessageBean msg2;

    @Autowired  // setter injection
    public void setMsg2(MessageBean msg2) {
        this.msg2 = msg2;
    }
    // Spring 4.3 <= 부터는
    // 생성자가 '하나만' 있는 경우 생성자 @Autowired 생략가능

    // 기본적으로 자동주입(autowired) 는
    // 1. 같은 타입의  bean 이 주입
    // 2. 같은 타입이 여러개 이면, name 이 같거나 (혹은 유사) 한것이 주입
    // 3. @Qualifier, @Primary 등으로 특정 이름의 bean 주입

    public DIMain05(Student stu1){
        System.out.println("DIMain05(Student) 생성");
        this.stu1 = stu1;
    }
    @Autowired  // constructor injection
    public DIMain05(@Qualifier("사천왕") Student stu1, Student stu2){
        System.out.println("DIMain05(Student, Student) 생성");
        this.stu1 = stu1;
        this.stu2 = stu2;
    }
    public static void main(String[] args) {
        System.out.println("Main시작");
        SpringApplication.run(DIMain05.class, args);
        System.out.println("Main종료");
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(stu1);
        System.out.println(stu2);

        msg1.sayHello();
        msg2.sayHello();
    }
}
