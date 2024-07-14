package com.lec.spring.controller2;

import com.lec.spring.common.U;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Controller
public class BaseController {
    @RequestMapping("/member/search")
    @ResponseBody
    public String searchMember(){
        System.out.println("/member/search  searchMember() 호출");
        return "searchMember() 호출";
    }

    // ModelAndView 객체 사용
// '뷰(View)' 와 '데이터(Model)' 을 둘다 -> ModelAndView 에 세팅
    @RequestMapping(value = "/member/find")
    public ModelAndView findMember(){
        ModelAndView mv = new ModelAndView();

        mv.addObject("mbName", "삼성라이온즈");
        mv.addObject("mbDate", LocalDateTime.now());
        mv.setViewName("member/find");
//      ↑ 없는 경우 디폴트 뷰를 이용 => 디폴트뷰 : 경로에 사용된 뷰 이름 사용
        return mv;
    }

    // wildcard *, ** 사용 가능
    @RequestMapping("/member/action/*")
    @ResponseBody
    public String memberAction1(HttpServletRequest request) {
        return U.requestInfo(request);
    }

    @RequestMapping("/member/action/*/*")
    @ResponseBody
    public String memberAction2(HttpServletRequest request) {
        return U.requestInfo(request);
    }

    @RequestMapping("/member/action/*/*/**")
    @ResponseBody
    public String memberAction3(HttpServletRequest request) {
        return U.requestInfo(request);
    }

    // 확장자 패턴 사용 가능
    @RequestMapping("/member/*.do")
    @ResponseBody
    public String doMember(HttpServletRequest request){
        return U.requestInfo(request);
    }

}
