package com.lec.spring.controller;

import com.lec.spring.domain.Post;
import com.lec.spring.domain.PostValidator;
import com.lec.spring.service.BoardService;
import com.lec.spring.util.U;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;  // 서비스 단 위임을 위해 만듦

    public BoardController() {
        System.out.println("BoardController() 생성");
    }

    @GetMapping("/write")
    public void write() {
    }

    @PostMapping("/write")
    public String writeOk(
            @RequestParam Map<String, MultipartFile> files, // 첨부파일들 <name, file>
            @Valid Post post  // @Valid : Post 객체의 유효성을 검증
            , BindingResult result // 유효성 검사 결과를 담을 객체, 유효성 검사가 실패하면, 이 객체에 오류 정보가 담긴다.
            , Model model  // 매개변수 선언시 BindingResult 보다 Model 을 뒤에 두어야 한다.
            , RedirectAttributes redirectAttributes    // redirect 시 넘겨줄 값들을 담는 객체
    ) {

        // 만약 에러가 있다면 redirect 한다!  즉, if 문 아래 코드를 실행하지 않는다 (유효성 검사 결과 처리)
        if (result.hasErrors()) {
            // 유효성 검사가 실패한 경우

            // addAttribute
            //    request parameters로 값을 전달.  redirect URL에 query string 으로 값이 담김
            //    request.getParameter에서 해당 값에 액세스 가능
            // addFlashAttribute
            //    일회성. 한번 사용하면 Redirect후 값이 소멸
            //    request parameters로 값을 전달하지않음
            //    '객체'로 값을 그대로 전달

            // redirect 시, 기존에 입력했던 값들은 보이게 하기 ( 리다이렉트된 페이지로 전달)
            redirectAttributes.addFlashAttribute("user", post.getUser());
            redirectAttributes.addFlashAttribute("subject", post.getSubject());
            redirectAttributes.addFlashAttribute("content", post.getContent());

            // 어떤 에러가 발생했는지 알려주기 위해 에러 내용을 같이 보내주기위함    (발생한 오류 메시지들을 전달)
            List<FieldError> errList = result.getFieldErrors();
            for (FieldError err : errList) {
                redirectAttributes.addFlashAttribute("error_" + err.getField(), err.getCode());
            }

            return "redirect:/board/write"; // 다시 /board/write 로 돌아온다 (이전에 입력한 내용들을 유지시켜주기 위한 작업 필요, redirectAttributes)
        }

        // 유효성 검사 성공
        model.addAttribute("result", boardService.write(post, files));
        return "board/writeOk";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {

        Post post = boardService.detail(id);
        model.addAttribute("post", post);

        return "board/detail";
    }

    @GetMapping("/list")
//    public void list(Model model){
    public void list(Integer page, Model model) {
        boardService.list(page, model);
//        model.addAttribute("list", boardService.list());
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        model.addAttribute("post", boardService.selectById(id));
        return "board/update";
    }

    @PostMapping("/update")
    public String updateOk(
            @RequestParam Map<String, MultipartFile> files,     // 새로 추가될 파일들
            @Valid Post post // 데이터들이 담긴 post 객체를 @Valid 를 통해 유효성 검사
            , BindingResult result   // 유효성 검사 결과를 담을 객체
            , Long [] delfile   // 삭제될 파일들
            , Model model
            , RedirectAttributes redirectAttributes) {   // redirect 시 넘겨줄 데이터들을 담는 객체

        if (result.hasErrors()) {

            redirectAttributes.addFlashAttribute("subject", post.getSubject());
            redirectAttributes.addFlashAttribute("content", post.getContent());

            // 어떤 에러가 발생했는지 알려주기 위해 에러 내용을 같이 보내주기위함    ( 발생한 오류 메시지 전달)
            List<FieldError> errList = result.getFieldErrors();
            for (FieldError err : errList) {
                redirectAttributes.addFlashAttribute("error_" + err.getField(), err.getCode());
            }

            return "redirect:/board/update/" + post.getId(); // Id는 수정사항이 아니다 ( 같이 넘겨주기 위해 추가 적으로 작성 )
        }

        model.addAttribute("result", boardService.update(post, files, delfile));
        return "board/updateOk";
        // 성공하면 detail 실패하면 update로 돌아가
    }

    @PostMapping("/delete")
    public String deleteOk(Long id, Model model) {
        model.addAttribute("result", boardService.deleteById(id));
        return "board/deleteOk";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {  // PostValidator 등록
        System.out.println("BoardController.initBinder() 호출");
        binder.setValidator(new PostValidator());
    }

    // 페이징
    // pageRows 변경시 동작
    @PostMapping("/pageRows")
    public String pageRows(Integer page, Integer pageRows) {
        U.getSession().setAttribute("pageRows", pageRows);  // 세션에 페이지번호를 담아 재요청
        return "redirect:/board/list?page=" + page;
    }
} // end controller




