package com.lec.spring.config;

import com.lec.spring.jwt.JWTUtil;
import com.lec.spring.jwt.LoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//@EnableWebSecurity(debug = true)  // 요청시 Security Filter Chain 의 동작 확인 출력
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // csrf disable
        http.csrf((auth) -> auth.disable());

        // 웹서비스에서 사용할수 있는 여러 인증방법들..
        //  폼 인증, Http basic 인증, OAuth2 인증, JWT인증 등...
        //  이번 예제에선 JWT 인증을 사용할 것이므로
        // Form 인증방식과 http basic 인증방식은 disable 시켜야 한다.

        // Form 인증방식 disable
        http.formLogin((auth) -> auth.disable());

        // Http basic 인증방식 disable
        http.httpBasic((auth) -> auth.disable());

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/member").hasAnyRole("MEMBER", "ADMIN")
                        .anyRequest().permitAll()
                );

        // 세션 설정
        http
                .sessionManagement((session) -> session
                        // JWT를 통한 인증/인가를 위해서 세션을 STATELESS 상태로 설정한다
                        // request 가 들어오면 세션을 생성했다가.  request 가 다 처리 되면 세션을 삭제하게 된다
                        //  명심! request 처리전까지는 세션이 존재하는 거구. 이 세션에는 SecurityContextHolder 에 인증정보가 있는 것이다.
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        //필터 추가 LoginFilter()는 AuthenticationManager 인자를 받음
        // authenticationManager() 메소드에 authenticationConfiguration 객체를 넣어야 함) 따라서 등록 필요
        // .addFilterAt(필터, 삽일할 위치)
        //   LoginFilter 를 SecurityFilter Chain 의 UsernamePasswordAuthenticationFilter 위치에 삽입 (그 위치를 replace 하게된다!)
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil),
                        UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

}







