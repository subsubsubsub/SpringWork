package com.lec.spring.controller3;

import com.lec.spring.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.*;

// 기본적으로 템플릿 파일의 루트는
// resources/templates 다.  이하 {templates} 로 표시
// 우리는 templates/views로 변경하여 사용하는중...

@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafController {

    //--------------------------------------------------------
// 데이터 준비
    String[] arr1 = new String[]{"AAA", "BBB", "CCC", "DDD", "EEE"};
    Member[] arr2;
    List<Integer> list1 = new ArrayList<>();
    List<Member> list2 = new ArrayList<>();


    Map<Integer, String> map1 = new HashMap<>();
    Map<String, Member> map2 = new HashMap<>();

    public ThymeleafController() {
        System.out.println(getClass().getName() + "() 생성");


        Member member;
        // 데이터 초기화
        for (int i = 0; i < 5; i++) {
            list1.add(i * 1000);
            member = new Member(100 + i, "u0" + i, "p0" + i, arr1[i],
                    LocalDateTime.now());
            list2.add(member);
            map1.put(i, arr1[i]);
            map2.put(arr1[i], member);
        }
        arr2 = list2.toArray(new Member[5]);


        // 확인해보기
        System.out.println("arr1: " + Arrays.toString(arr1));
        System.out.println("arr2: " + Arrays.toString(arr2));


        System.out.println("list1: " + list1);
        System.out.println("list2: " + list2);


        System.out.println("map1: " + map1);
        System.out.println("map2: " + map2);


    } // end 생성자

    @RequestMapping("/sample1")
    public void sample1(Model model) {
        // "/thymeleaf/sample1.html" 을 찾아서 템플릿 엔진으로 보냄.
        model.addAttribute("greeting", "Hello Thymeleaf");
        model.addAttribute("date", LocalDateTime.now());
    }

    /*********************************************************
     * Standard Expression Syntax
     *      - Literals
     *      - Variable Expressions: ${...}
     */
    /**************************************************************
     * Conditional expressions
     *      If-then:(if) ? (then)
     *      If-then-else: (if) ? (then) : (else)
     *         Default: (value) ?: (defaultvalue)
     */
    /* *****************************************************
     * Variable Expressions: ${...}
     *   . (dot) 을 사용하여 접근
     *
     *   1. Object 변수의 .property → 해당 Object 의 property 접근 (getter 호출값)
     *         없는 property 는 에러
     *   2. 배열, List<> 변수의 [index] →  index 번째의 item 값
     *          없는 index 는 에러
     *   3. Map<k,v> 변수의 .key →  Map 의 value 값
     *          없는 key 는 에러
     */
    @RequestMapping("/sample2")
    public void sample2(Model model) {
        Member member = new Member(123, "u00", "p00", "홍길동", LocalDateTime.now());

        model.addAttribute("m", member);  // thymeleaf 에선 m 이라는 이름의 변수로 접근 가능.
        System.out.println(null + "조창성");
//        System.out.println(null + null);

        System.out.println(model.addAttribute("arr1", arr1));
        System.out.println(model.addAttribute("arr2", arr2));
        System.out.println(model.addAttribute("list1", list1));
        System.out.println(model.addAttribute("list2", list2));
        System.out.println(model.addAttribute("map1", map1));
        System.out.println(model.addAttribute("map2", map2));
    }

    // th:each
    // #temporals : java.time.* 객체를 다루기 위한 Expression Utility Objects (표현식 유틸 객체)
    @RequestMapping("/sample3")
    public void sample3(Model model) {
        model.addAttribute("list", list2);
    }

    // th:with  변수선언
    // th:if, th:unless   조건부 렌더링
    // #strings  :  문자열 다루기 위한 표현식 유틸리티 객체
    // #strings.isEmpty

    @RequestMapping("/sample4")
    public void sample4(Model model) {
        list2.get(3).setId(null);
        model.addAttribute("list", list2);
        model.addAttribute("map2", map2);

        // th:if  에서 어케 판정될까?
        model.addAttribute("test1", "aaa");     // 참
        model.addAttribute("test2", "");        // 참
        model.addAttribute("test3", null);      // 거짓
        model.addAttribute("test4", false);     // 거짓
    }

    /* *********************************************************************
     * Inlining
     *  기본적으로 thymeleaf 는 tag attribute 에 지정해서 동작하나,  직접 HTML 에 작성하는 inline 방식도 제공합니다
     *  https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#inlining
     *
     * 4가지 종류의 inline ↓
     *
     *  1. Expression inlining
     *     [[...]] ← th:text 와 같은 동작
     *     [(...)] ← th:utext 와 같은 동작
     *  2. Text inlining →  th:inline="text"
     *  3. JavaScript inlining → <script th:inline="javascript">
     *  4. CSS inlining → <style th:inline="css">
     */

    // tag attribute 방식 vs. inline 방식
    @GetMapping("/sample5")
    public void sample5(Model model) {
        String result = "SUCCESS";
        model.addAttribute("result", result);
        model.addAttribute("list", list2);
        model.addAttribute("msg", "<b>great</b>");
    }

    // Expression Utility Object
    @GetMapping("/sample6")
    public void sample6(Model model) {
        model.addAttribute("now1", LocalDateTime.now());    // java.time.LocalDateTime
        model.addAttribute("now2", new Date());   // java.util.Date
        model.addAttribute("price", 123456789);
        model.addAttribute("title", "This is sample");
        model.addAttribute("options", Arrays.asList("BBB", "DDD", "AAAA", "CCC"));  // List<>
    }

    /****************************************************
     * Link URLs
     *     Absolute URL   @{http:// … }   ← protocal 로 시작
     *     Context-relative URL    @{/ … }  ←  / 으로 시작
     *     Server-relative URL     @{~ … }  ←  ~ 으로 시작
     */
    @GetMapping("/sample7")
    public void sample7(Model model, HttpServletRequest request) {
        model.addAttribute("relativeURL", "sample1");
    }

    /****************************************************
     * Link URLs
     *     Absolute URL   @{http:// … }   ← protocal 로 시작
     *     Context-relative URL    @{/ … }  ←  / 으로 시작
     *     Server-relative URL     @{~ … }  ←  ~ 으로 시작
     */

    @GetMapping("/sample8")
    public void sample8(Model model) {
        model.addAttribute("value1", "John");
        model.addAttribute("url1", "sample01");
        model.addAttribute("select1", "volvo");
    }


    /* **************************************************
     * Template layout
     *   https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#template-layout
     *
     * Including template fragments
     *
     *  ~{...}  ← Fragment expression
     *    https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#fragments
     *
     *  include, replace 되는 fragment 도 바깥쪽 변수 사용 가능.
     */

    @GetMapping("/sample10")
    public void sample10(Model model) {
        model.addAttribute("value1", "John");
        model.addAttribute("url1", "sample1");
    }

}   // end controller
