package com.lec.spring.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDenideHandler implements AccessDeniedHandler {

    // 권한이 없는 url 접근을 할때 호출
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        System.out.println("### 접근 권한 오류 : CustomAccessDenideHandler : " + request.getRequestURI() + "###");

        response.sendRedirect("/user/rejectAuth");  // 권한이 없는 경우 해당 url 로 이동시켜
    }
}
