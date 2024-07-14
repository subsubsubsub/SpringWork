package com.lec.spring.controller1;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * org.springframework.http 패키지의 주요 객체
 * <p>
 * HttpEntity<T> :
 * │          HTTP request, 혹은 HTTP response 표현객체. 'headers' 와 'body' 로 구성됨
 * │          request 와 response 를 보다 세밀하게 제어할수 있다.
 * │                  └ ① header 설정
 * │                  └ ② body 설정
 * │
 * │          SpringMVC @Controller 메소드의 return 값으로도 사용
 * │          RestTemplate 에서도 활용
 * │          https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpEntity.html
 * │
 * └─ RequestEntity<T>
 * │          HTTP request 표현객체
 * │          SpringMVC @Controller 메소드의 '입력값', RestTemplate 에서 활용
 * │          https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/RequestEntity.html
 * │
 * └─ ResponseEntity<T>
 * HTTP response 표현객체
 * Status Code 도 설정 가능
 * SpringMVC @Controller 메소드의 '리턴값', RestTemplate 에서 활용
 * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html
 * <p>
 * MultiValueMap (I)
 * └─ HttpHeaders
 * Http header(들) 표현 객체
 * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpHeaders.html
 * <p>
 * MediaType
 * Http 요청과 응답의 Content type 표현 객체
 * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/MediaType.html
 */

@Controller
public class HomeController2 {
    public final String str1 = "<h1>안녕하세요 Hello!</h1>";

    String[] jsonArr = {
            // [0]
            """
           {
               "status": "성공",
               "code": 34200,
               "response_time": "2023-07-03 13:09:06"
           }
           """,
    };

    @RequestMapping("http01")
    @ResponseBody
    public HttpEntity<String> http01() {
        String body = str1;

        // HttpEntity<T> : T 는 response 할 body 의 타입
        HttpEntity<String> entity = new HttpEntity<>(body);

        return entity;
        // 기본적으로 Content type 은 text/html;Charset=UTF-8 로 response 된다. (개발자 도구 - Network)
    }


    @RequestMapping("http02")
    @ResponseBody
    public HttpEntity<String> http02() throws UnsupportedEncodingException {    // 원하는 header 추가 가능 // 특정 header 가 없으면 브라우저에서 동작하게 하지 않는 등의 활용가능
        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.TEXT_PLAIN);   // text/plain 으로 response // 깨져서 출력됨

        headers.add("Content-Type", "text/plain;charset=UTF-8");    // 한글 정상 출력 // utf-8 하지 않으면 2 byte 인코딩 한 값이 전달되어 한글이 깨져서 나옴
        headers.add("user-secret", "xxxx");
//        headers.add("my-team", "삼성라이온즈");   // Error
        headers.add("my-team", URLEncoder.encode("삼성라이온즈", "utf-8"));

        String body = str1;

        // HttpEntity<T> : T 는 response 할 body 의 타입
        HttpEntity<String> entity = new HttpEntity<>(body, headers);    // HttpEntity(body, headers)

        return entity;
    }


    // 일반적으론 response 는  ResponseEntity 사용
    @RequestMapping("http03")
    @ResponseBody
    public ResponseEntity<String> http03() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);     // application/json     // 기본적으로 utf-8로 response

        String body = jsonArr[0];
        ResponseEntity<String> entity = new ResponseEntity<>(body, headers, HttpStatus.OK);     // (body, header, statusCode)   // statusCode : enum 타입
        return entity;
    }


    // body 에 어떠한 Java 객체도 가능
    @RequestMapping("http04")
    @ResponseBody
    public ResponseEntity<HomeController.Product> http04() {
        HomeController.Product product = new HomeController.Product(10, "신성태", false);
        ResponseEntity<HomeController.Product> entity = new ResponseEntity<>(product, HttpStatus.OK);
        return entity;
    }


    // <T> 타입 명시하기 귀찮다면...
    // type parameter 를 ? (혹은 Object) 로 작성해두면 편리하다 (일반적으로 많이 쓰임)
    // ※ 개발단계에서 response body 의 타입은 유동적으로 변할수 있기 때문이다.

    @RequestMapping("http05")
    @ResponseBody
    public ResponseEntity<?> http05() { // 어떤 타입을 return 해도 상관없다. <?>
        var product = new HomeController.Product(11, "바이크", true);
        ResponseEntity<?> entity = new ResponseEntity<>(product, HttpStatus.OK);
        return entity;
    }

    // 다양한 statusCode 값 명시하여 response 가능
    @RequestMapping("http06")
    @ResponseBody
    public ResponseEntity<?> http06() {
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);  // 400 Error
//        new ResponseEntity<>(null, HttpStatus.NOT_FOUND);  // 404
//        new ResponseEntity<>(null, HttpStatus.CONFLICT);  // 409
    }


    @RequestMapping("http07")
    @ResponseBody
    public void http07() {
        // TODO
    }


}
