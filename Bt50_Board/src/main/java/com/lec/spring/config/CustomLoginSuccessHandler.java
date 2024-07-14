package com.lec.spring.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.time.LocalDateTime;
import java.util.*;

import java.io.IOException;

/*
로그인 성공 시 클라이언트의 IP 주소와 사용자 정보를 출력
로그인 시간을 세션에 저장
기본 성공 핸들러 동작을 유지하여 로그인 전 요청했던 URL로 리디렉션
클라이언트의 실제 IP 주소를 가져오는 유틸리티 메서드 제공
* */

// 사용자가 성공적으로 로그인하 후에 추가적인 동작을 정의하는 Spring Security의 커스텀 로그인 성공 핸들러
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    public CustomLoginSuccessHandler(String defaultTargetUrl) {
        // SavedRequestAwareAuthenticationSuccessHandler#setDefaultTargetUrl()
        // 로그인후 특별히 redirect 할 url 이 없는경우 기본적으로 redirect 할 url

        setDefaultTargetUrl(defaultTargetUrl);
    }

    // 로그인 성공 직후 수행할 동작 ( 로그인 성공 시 호출 되는 메서드)
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
                                                                                                  // Authentication : 세션에 저장되어 있는 놈이 이놈이에요.

        System.out.println("로그인 성공: onAuthenticationSuccess() 호출 ###");

        System.out.println("접속IP: " + getClientIp(request));    // 로그인 시 클라이언트의 IP 주소를 출력
        PrincipalDetails userDetails = (PrincipalDetails)authentication.getPrincipal(); // 인증된 사용자 정보를 가져와 userDetails에 입력
        System.out.println("username: " + userDetails.getUsername());
        System.out.println("password: " + userDetails.getPassword());
        List<String> roleNames = new ArrayList<>();   // 권한이름들
        authentication.getAuthorities().forEach(authority -> {
            roleNames.add(authority.getAuthority());
        });
        System.out.println("authorities: " + roleNames);

        // 로그인 시간을 세션에 저장하기 (※ logout 예제에서 사용)
        LocalDateTime loginTime = LocalDateTime.now();  // 현재 로그인 시간을 가져와 세션에 저장
        System.out.println("로그인 시간: " + loginTime);
        request.getSession().setAttribute("loginTime", loginTime);
        // request.getSession : http 세션 객체가 리턴되요

        // 로그인 직전 url 로 redirect 하기
        super.onAuthenticationSuccess(request, response, authentication);
    }

    // request 를 한 client ip 가져오기
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


}
