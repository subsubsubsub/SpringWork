package com.lec.spring.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    // 로그아웃 성공 직후 호출되는 메서드
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("### 로그아웃 성공 : CustomLogoutSuccessHandler 동작 ###");

        // 로그아웃 시간 남기기
        LocalDateTime logoutTime = LocalDateTime.now();
        System.out.println("로그아웃시간: " + logoutTime);

        // 사용시간 (로그인 ~ 로그아웃) 계산해보기
        LocalDateTime loginTime = (LocalDateTime) request.getSession().getAttribute("loginTime"); // 세션에서 로그인한 시간 꺼내오긔
        if (loginTime != null) {
            // 그냥 하면 찍히지 않는다 => 로그아웃 하면 세션이 지워지기 때문에 그 안에 있는 로그인 시간이 날라간다. => getSession 이 null 리턴
            //.invalidateHttpSession(false) 를 통해 세션을 유지시켜 줘야한다.
            long seconds = loginTime.until(logoutTime, ChronoUnit.SECONDS);   // 로그인 시간 부터 로그아웃 시간까지 초단위로 계산해서 반환
            System.out.println("사용시간: " + seconds + "초");
        }
        request.getSession().invalidate();  // session invalidate => 세션 수동 삭제

        String redirectUrl = "/user/login?logoutHandler";    // 로그아웃 직후 이동 url

        // ret_url 이 있는 경우 logout 하고 해당 url 로 redirect
        if (request.getParameter("ret_url") != null) {  // 파라미터에 url이 남아있으면 해당 url로 이동
            redirectUrl = request.getParameter("ret_url");
        }

        // redirect 수동 실행
        response.sendRedirect(redirectUrl);
    }
}
