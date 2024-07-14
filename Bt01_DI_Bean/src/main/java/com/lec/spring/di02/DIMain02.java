package com.lec.spring.di02;

import com.lec.spring.beans.MessageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DIMain02 implements CommandLineRunner {

    public MessageBean msg;

    @Autowired  // 자동주입 (field injection)
    ApplicationContext ctx;// 스프링 컨테이너, 컨텍스트, IoC 컨테이너, Bean Factory 등 지칭하는 용어 다양

    @Autowired  // 자동주입 (setter injection)
    public void setMsg(MessageBean msg) {
        System.out.println("setMsg() 호출");
        this.msg = msg;
    }

    public DIMain02(){
        System.out.println(getClass().getName() + "() 생성");     // @SpringBootApplication 이 있으면 자동으로 bean객체로 생성
                                                                    // 컨테이너 중간에 실행
        
    }


    public static void main(String[] args) {
        System.out.println("Main() 시작");
        SpringApplication.run(DIMain02.class, args);
        System.out.println("Main() 종료");
    }
    // 컨테이너 실행 후 실행
    @Override
    public void run(String... args) throws Exception {
        System.out.println("run() 실행"); // 컨테이너가 다 실행되고 나서 실행
        msg.sayHello();

        System.out.println("생성된 빈의 개수: " + ctx.getBeanDefinitionCount());

        // 모든 bean 에는 name(id) 이 부여된다.
        for (var name : ctx.getBeanDefinitionNames()) {
            System.out.println(name + " : " + ctx.getBean(name));
        }
    }
}
