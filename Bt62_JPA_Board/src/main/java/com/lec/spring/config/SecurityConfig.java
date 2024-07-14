package com.lec.spring.config;

import com.lec.spring.config.oauth.PricipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  // 이 클래스를 보안 용도로 사용하겠다 Annotation
public class SecurityConfig {

//    @Bean
//    public PasswordEncoder encoder() { // BCryptPasswordEncoder 를 빈으로 등록하여 암호를 암호화 한다.
//        return new BCryptPasswordEncoder();
//    }

    // ↓ Security 동작 시키지 않기
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer(){   // WebSecurityCustomizer 구현체 리턴
//        return web -> web.ignoring().anyRequest();  // 어떠한 request 도 Security 가 무시한다.
//    }

    //OAuth2 Client
    @Autowired
    private PricipalOauth2UserService pricipalOauth2UserService;

    // ↓ SecurityFilterChain 을 Bean 으로 등록해서 사용
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {   // 서버족에서 클라이언트에 어떠한 값을 심어넣고, request에 심어 놓은 값이 함께 담겨와야 서버쪽에서 request 처리
        return http
                .csrf(csrf -> csrf.disable())   // CSRF 비활성화    ( 실습과정에서 처리해야할 문제들이 많기때문에 편의상 비활성화 )
                /* *********************************************
                 * ① request URL 에 대한 접근 권한 세팅  : authorizeHttpRequests()
                 * .authorizeHttpRequests( AuthorizationManagerRequestMatcherRegistry)
                 **********************************************/
                .authorizeHttpRequests(auth -> auth
                        // URL 과 접근권한 세팅(들)
                        // ↓ /board/detail/** URL로 들어오는 요청은 '인증'만 필요.
                        .requestMatchers("/board/detail/**").authenticated()
                        // ↓ "/board/write/**", "/board/update/**", "/board/delete/**" URL로 들어오는 요청은 '인증' 뿐 아니라 ROLE_MEMBER 나 ROLE_ADMIN 권한을 갖고 있어야 한다. ('인가')
                        .requestMatchers("/board/write/**", "/board/update/**", "/board/delete/**").hasAnyRole("MEMBER", "ADMIN")
                        // ↓ 그 밖의 다른 요청은 모두 permit!
                        .anyRequest().permitAll()
                )
                /* *******************************************
                 * ② 폼 로그인 설정
                 * .formLogin(HttpSecurityFormLoginConfigurer)
                 *  form 기반 인증 페이지 활성화.
                 *  만약 .loginPage(url) 가 세팅되어 있지 않으면 '디폴트 로그인' form 페이지가 활성화 된다
                 ********************************************/
                .formLogin(form -> form
                                .loginPage("/user/login") // 로그인 필요한 상황 발생시 매개변수의 url (로그인 폼) 으로 request 발생
                                .loginProcessingUrl("/user/login")  // "/user/login" url 로 POST request 가 들어오면 시큐리티가 낚아채서 처리, 대신 로그인을 진행해준다(인증).
                                // 이와 같이 하면 Controller 에서 /user/login (POST) 를 굳이 만들지 않아도 된다!
                                // 위 요청이 오면 자동으로 UserDetailsService 타입 빈객체의 loadUserByUsername() 가 실행되어 인증여부 확인진행 <- 이를 제공해주어야 한다.
                                .defaultSuccessUrl("/") // '직접 /login' → /login(post) 에서 성공하면 "/" 로 이동시키기
                                // 만약 다른 특정페이지에 진입하려다 로그인 하여 성공하면 해당 페이지로 이동 (너무 편리!)

//                        .usernameParameter("aaa")   // 기본 name = "username"
//                        .passwordParameter("bbb") // 기본 name = "password

                                // 로그인 성공 직후 수행할 코드
                                //.successHandler(AuthenticationSuccessHandler)
                                .successHandler(new CustomLoginSuccessHandler("/home")) // 로그인 성공 시 커스텀 핸들러 사용. '/home'으로 리다이렉트
                                // 로그인 실패하면 수행할 코드
                                // .failureHandler(AuthenticationFailureHandler)
                                .failureHandler(new CustomLoginFailureHandler())    // 로그인 실패 시 커스텀 핸들러 사용
                )

                /* *******************************************
                 * ③ 로그아웃 설정
                 * .logout(LogoutConfigurer)
                 ********************************************/
                // ※ 아래 설정 없이도 기본적올 /logout 으로 로그아웃 된다
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                                .logoutUrl("/user/logout") // 로그아웃 수행 url 을 설정 // url 에 입력하면 logout 됨
                                .logoutSuccessUrl("/home")  // 로그아웃 성공후 redirect url

                                .invalidateHttpSession(false)   // session invalidate 수행 안함
                                // 이따가 CustomLogoutSuccessHandler 에서 꺼낼 정보가 있기 때문에
                                // false 로 세팅한다 // 로그아웃해도 세션을 유지, 추후 삭제 필요


//                        .deleteCookies("JSESSIONID")    // 특정 쿠키 제거


                                // 로그아웃 성공후 수행할 코드
                                // .logoutSuccessHandler(LogoutSuccessHandler)
                                // logoutSuccessHandler 가 있으면 logoutSuccessUrl 은 동작하지 않는다.
                                .logoutSuccessHandler(new CustomLogoutSuccessHandler())

                )
                /********************************************
                 * ④ 예외처리 설정
                 * .exceptionHandling(ExceptionHandlingConfigure)
                 ********************************************/
                // ※ 아래 설정이 없이 user2 로 /board/write 접근하면 403 에러 발생
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                        // 권한(Authorization) 오류 발생시 수행할 코드
                        // .accessDeniedHandler(AccessDeniedHandler)
                        .accessDeniedHandler(new CustomAccessDenideHandler())
                )

                /********************************************
                 * OAuth2 로그인
                 * .oauth2Login(OAuth2LoginConfigurer)
                 ********************************************/
                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
                        .loginPage("/user/login")   // 로그인 페이지를 기존과 동일한 url 로 지정
                        // ↑ 구글 로그인 완료된 뒤에 후처리가 필요하다!

                        // code 를 받아오는 것이 아니라, 'AccessToken' 과 사용자 '프로필정보'를 한번에 받아온다
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                // 인증서버의 userinfo endpoint 설정
                                .userService(pricipalOauth2UserService)  // userService(OAuth2UserService)
                        )
                )


                .build();
    }   // end filterChain()

    // -------------------------------------------------------------------
    //OAuth 로그인
    //AuthenticationManager Bean 생성
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
