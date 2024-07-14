package com.lec.spring.common;

import jakarta.servlet.http.HttpServletRequest;

public class U {
    // 현재 request url 과 어떤 method 를 처리하는 출력 도우미
    public static String requestInfo(HttpServletRequest request){


        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        return String.format("%s ] %s, %s()호출"
                , request.getMethod(), request.getRequestURI(), methodName);
    }

}
