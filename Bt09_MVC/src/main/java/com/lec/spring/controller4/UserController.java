package com.lec.spring.controller4;

import com.lec.spring.common.U;
import com.lec.spring.domain.Post;
import com.lec.spring.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/delete")      // "/user/delete"
    public void deMember(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");     // name 이 'id' 인  parameter 값 리턴 (★String)
        // 없으면 null 리턴  // 주소 창에 적은 id 값을 뽑아 여기에 넣어
        System.out.println("id = " + id);

        int age = Integer.parseInt(request.getParameter("age"));
        System.out.println("age = " + age);

        model.addAttribute("mdId", id);
        model.addAttribute("mdAge", age);

        // 동일한 name 의 parameter가 여러개 인 경우
        // getParameterValues(name) => String[] 리턴
        String[] ages = request.getParameterValues("age");
        System.out.println("ages = " + Arrays.toString(ages));
        model.addAttribute("ages", Arrays.toString(ages));
    }

    @GetMapping("/regist")
    public void registUser() {
    }

    @RequestMapping("/regOk")   // <-- 어떠한 request method 에서도 동작
    public void regOk(HttpServletRequest request, Model model) {
        System.out.println(U.requestInfo(request));
        String username = request.getParameter("username");
        model.addAttribute("username", username);
    }

    @GetMapping("/regOk2")
    public String regOk2Get(HttpServletRequest request, Model model) {
        System.out.println(U.requestInfo(request));
        String username = request.getParameter("username");
        model.addAttribute("username", username);

        return "user/regOk";
    }

    @PostMapping("/regOk2")
    public String regOk2Post(HttpServletRequest request, Model model) {
        System.out.println(U.requestInfo(request));
        String username = request.getParameter("username");
        model.addAttribute("username", username);

        return "user/regOk";
    }

    // parameter name 값과 동일한 이름의 매개변수를 핸들러에 지정해서 받아오기
    @RequestMapping("/find1")
    @ResponseBody
//    public String find1(String name, String id){
    public String find1(String id, String name) {    // 매개변수 순서 바뀌어도 OK!!
        // parameter 가 없으면 null~
        return "user/find1 : id = " + id + ", name = " + name;
    }

    // 숫자 타입은 바로 parsing 하여 받을 수 있다!
//    new *
    @RequestMapping("/find2")
    @ResponseBody
//    public String find2(double id, String name){    // primitive 타입인데, parameter 에 없는 경우, 혹은 parsing 불가능하면 에러 발생
    public String find2(Double id, String name) {
//        숫자 타입을 wrapper 로  지정하는경우 parameter에 입력하지 않아도 null 값을 받아올 수 있다.
        return "user/find2 : id =" + id + ", name = " + name;
//        http://localhost:8080/user/find2?id=789&name=신우섭 => user/find2 : id =789.0, name = 신우섭
    }
    // parameter 를 특정 타입의 자바객체로 변환하여 받아내는 것을 'binding' 한다고 한다
    // parameter "name"-"value"  -->  Java 변수, 객체

    // 동일한 name 의 parameter(들) --> '배열' 혹은 String 매개변수로 받을수 있다.
    @RequestMapping("/find3")
    @ResponseBody
    public String find3(Integer[] id, String name) {
        return "user/find3 : id = " + Arrays.toString(id) + ", name = " + name;
    }

    // 만약 request parameter 의 name 과 매개변수가 같을수 없는 상황이면
    // @RequestParam 어노테이션 사용
    @RequestMapping("/find4")
    @ResponseBody
    public String find4(
            @RequestParam(value = "id", required = false, defaultValue = "UNKNOWN") String userid   // "id" 란 name 의 parameter 값을 userid 매개변수에 binding 해준다
            , @RequestParam("name") String username
    ) {
        return "/user/find4 : id = " + userid + ", name = " + username;
    }
    // 위의 경우 id 값이 없거나 변환 불가능하면 에러 발생한다.
    // (왜냐하면 @RequestParam 의 required=true 이기 때문이다)
    // @RequestParam(value="test", required=false, defaultValue="0") 을 이용하면 가능하긴 하다. => 없으면 defaultValue

    // 또한, @RequestParam 과 Map<name, value> 을 사용하면 된다.
    // Map<> 은 어떠한 parameter도 다 받아낸다아아아아아아
    @RequestMapping("/find5")
    @ResponseBody
    public String find5(@RequestParam Map<String, String> map) {
        return "user/find5: " + map;
    }

//--------------------------------------------------------------
// Java 객체로 받기 (aka. 커맨드객체 (command object)
//    (parameter를 받아낸 Java객체를 만들어 놓긔)

// 자바객체(커맨드 객체) 사용
// 코드 작업량이 매우 줄어든다.


// Java 객체에 바인딩될때
// 내부적으로 '기본생성자로 생성' 한뒤
// set property 를 사용하여 각 parameter 를 Java객체에 binding 합니다


// 커맨드 객체는 기본적으로 '객체타입명'으로 Model attribute 추가 된다. (소문자로)
// Model attribute name 을 바꿀경우 @ModelAttribute 로 지정


    // 게시글 등록 form
    @GetMapping("/write")
    public void writeBoard() {
    }


    // 자바객체(커맨드 객체) 사용
    // 코드 작업량이 매우 줄어든다.

    // Java 객체에 바인딩될때
    // 내부적으로 '기본생성자로 생성' 한뒤
    // set property 를 사용하여 각 parameter 를 Java객체에 binding 합니다

    // 커맨드 객체는 기본적으로 '객체타입명'으로 Model attribute 추가 된다. (소문자로)
    // Model attribute name 을 바꿀경우 @ModelAttribute 로 지정
    @PostMapping("/writeOk")
    public void writeOkBoard(@ModelAttribute("DTO") Post post) {
        System.out.println("/writeOk : " + post);
    }

//--------------------------------------------------------------
    /*
     * @RequestBody 가 핸들러의 파라미터에 붙으면
     *     HTTP요청의 본문(body)이 그대로 전달
     *     xml 이나 json 기반의 메시지를 사용하는 경우 유용.
     */

    @PostMapping("/test01")
    @ResponseBody
    public User userTeste01(int age, String name) {
        User user = new User(age, name);
        return user;
    }
    // POST 요청
    //  ① query string  ?age=10&name=Juni  OK
    //  ② x-www-form-urlencoded 방식  OK
    //  ③ form-data 방식  OK
    //  ④ raw-json  (불가!)  500에러

    @PostMapping("/test02")
    @ResponseBody
    public User userTest02(User user) {
        return user;
    }
    // POST 요청
    //  ① query string  ?age=10&name=Juni  OK
    //  ② x-www-form-urlencoded 방식  OK
    //  ③ form-data 방식  OK
    //  ④ raw-json => 모두 null 값 (binding 된게 없다!)

    @PostMapping("/test03")
    @ResponseBody
    public User userTest03(@RequestBody User user) {
        return user;
    }
    // POST 요청
    //  ① query string  ?age=10&name=Juni  (에러)
    //  ② x-www-form-urlencoded 방식  (에러)
    //  ③ form-data 방식  (에러)
    //  ④ raw-json => OK

    // @RequestHeader(key) : header value 도 받아올 수 있다.
    @PostMapping("/test04")
    @ResponseBody
    public String userTest04(
            @RequestBody User user,
            @RequestHeader("x-auth") String auth,
            @RequestHeader(value = "x-key", required = false, defaultValue = "FUCK SHIT") String key
    ) {
        return """
                User : %s
                auth : %s
                key : %s
                """.formatted(user, auth, key);
    }
    //----------------------------------------------------------------------------
    /*
     * @PathVariable 사용
     *
     *  request url 을 통해 parameter 를 받는 방법은 다음 방법들이 있다
     *   /API_NAME?key1=val1   <-- query string 사용
     *   /API_NAME/{value1}    <-- path variable 사용
     */

    @RequestMapping("/writePath/{name}/{subject}/{k3}")
    @ResponseBody
    public String wrtePathBoard(
            // 경로상에 지정한 값들이 꽂힌댄다
            @PathVariable String name,
            @PathVariable String subject,
            @PathVariable(name = "k3") String content
            // k3를 content에 꽂는다
    ) {
        return """
                name: %s <br>
                subject: %s <br>
                content: %s <br>
                """.formatted(name, subject, content);
    }

//-----------------------------------
// redirect
//  "redirect:{url}" 을 리턴 => redirect 를 response
//   redirect 의 response code 는 3xx


    @RequestMapping("/ageInput")
    public void ageInput() {
    }

    @RequestMapping("/ageCheck")
    public String chkAge(int age
            , RedirectAttributes redirectAttrs) {   // redirect 되는 request 에 담을 parameter 지정

        redirectAttrs.addAttribute("age", age); // "age" 라는 parameter 지정, query string 형식으로 전달됨.
        // redirect 되는 request에 담김
//        redirectAttrs.addFlashAttribute("age", age); // 전달은 되지만 주소창에 표시 되지 않는다. (보안)

        if (age < 19) {
            return "redirect:/user/underAge";   // view 를 리턴하는게 아니라 redirect 하게 된다
        } else {
            return "redirect:/user/adult";
        }
    }

    @RequestMapping("/underAge")
    @ResponseBody
    public String pageUnderAge(int age) {
        return """
                미성년자입니다.<br>
                나이: %s 살 , %s 년 뒤에 사용 가능 <br>
                """.formatted(age, 19 - age);
    }

    @RequestMapping("/adult")
    @ResponseBody
    public String pageAdult(int age) {
        return """
                성인입니다.<br>
                나이 : %d 살
                """.formatted(age);
    }
    // ------------------------------------------------------
    // forward

    @RequestMapping("/detail")
    public String memberDetail() {
        System.out.println("/user/detail 요청");
        return "forward:/user/notfound";
    }

    @RequestMapping("/notfound")
    @ResponseBody
    public String memberNotFound() {
        System.out.println("/user/notfound 요청");
        return "/user/notfound";
    }
}   // end Controller
