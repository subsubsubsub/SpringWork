package com.lec.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing  // 스프링에서 제공하는 기본 Entity Listner 사용하기 위해
public class Bt17JpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(Bt17JpaApplication.class, args);
    }

}
