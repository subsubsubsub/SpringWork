package com.lec.spring.di07;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;

/*
 * @Value
 *   org.springframework.beans.factory.annotation.Value  (Lombok 의 Value 와 헷갈리지 말자)
 *
 *   스프링 빈 의 '필드' 등에 '값' 을 주입하는데 사용
 *   주입대상: 필드, 생성자, 메소드 매개변수
 *
 *   참조:
 *      https://www.baeldung.com/spring-value-annotation
 *
 *
 */

@SpringBootApplication
public class DIMain07 implements CommandLineRunner {

    enum PRIORITY{HIGH, LOW};

    @Value("${value.from.file}")   // application.properties에 키 값을 넣어준다.     // value 값은 한글 불가능
    private String value1;

    @Value("${app.server.port}")
    String port1;

    // 같은 키값 주입 가능
    @Value("${app.server.port}")
    String port2;

    @Value("${app.server.port}")
    int port3; // 자동 형변환 가능

    // 없는 key 값은 에러!
//    @Value("${app.port}")   // Could not resolve placeholder 'app.port' in value "${app.port}"
//    int port4;

    // default 값을 줄 수 있다.
    @Value("${app.port:100}")
    int port4;

    @Value("${priority}")
    String pr1;

    @Value("${priority}")
    PRIORITY pr2;

    @Value("${app.values1}")
    String[] arr1;

    @Value("${app.values1}")
    List<String> list1;


    @Override
    public void run(String... args) throws Exception {

        System.out.println(value1);
        System.out.println(port1);
        System.out.println(port2);
        System.out.println(port3);
        System.out.println(port4);

        System.out.println("pr1: " + pr1);
        System.out.println("pr2: " + pr2);

        System.out.println(Arrays.toString(arr1));
        System.out.println(list1);

    }


    public static void main(String[] args) {
        System.out.println("Main시작");
        SpringApplication.run(DIMain07.class, args);
        System.out.println("Main종료");
    }
}


