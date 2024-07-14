package com.lec.spring.controller2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

//@Controller
//@RequestMapping("/board")
//public class BoardController {
//
//    @RequestMapping("/list")
//    public void listBoard(){
//    }
//    @RequestMapping("/write")
//    public void writeBoard(){
//    }
//    @RequestMapping("/detail")
//    public void detailBoard(){
//    }
//    @RequestMapping("/update")
//    public void updateBoard(){
//    }
//    @RequestMapping("/delete")
//    public void deleteBoard(){
//    }
//}
@Controller
@RequestMapping("/sts10_request")
public class BoardController{

    @RequestMapping("/board/list")
    public void listBoard(){
    }
    @RequestMapping("/board/write")
    public void writeBoard(){
    }
    @RequestMapping("/board/detail")
    public void detailBoard(){
    }
    @RequestMapping("/board/update")
    public void updateBoard(){
    }
    @RequestMapping("/board/delete")
    public void deleteBoard(){
    }
}

