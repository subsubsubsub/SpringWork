package com.lec.spring.controller2;

import com.lec.spring.common.U;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  일반적인 url 구성은
 *     /{controller}/{action} <- 이와 같은 방식으로 구성하곤 한다
 *
 *    - 그리고, 특정 도메인에 대한 동작을 특정 controller 에 전담하여 설계하는게 일반적
 *      ex)  /member/find, /member/do, /member/list
 *           => MemberController 가 담당!
 *
 *    - 매 url 마다 서두에 반복되는  "/member" 를 handler 가 아닌 controller 에 지정 가능
 */

@Controller
@RequestMapping("/member")  // ← 이 컨트롤러는 "/member" 로 시작하는 url 에 매핑된다.
public class MemberController {

    @RequestMapping("/save")    // URL mapping -> "/member" + "/save" => "/member/save"
    @ResponseBody
    public String saveMember(HttpServletRequest request) {

        return U.requestInfo(request);
    }

    @RequestMapping("/load")   //  /member + /load => /member/load
    public void loadMember(){
        // return "/member/load" 를 리턴한것과 동일
    }

//    @RequestMapping("/search")
//    public void searchMember(){ // /member/search => 에러~ ("/search" 는 이미 존재한다)
//
//    }
}
