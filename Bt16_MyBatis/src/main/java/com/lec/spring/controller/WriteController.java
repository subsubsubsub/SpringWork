package com.lec.spring.controller;

import com.lec.spring.domain.Write;
import com.lec.spring.domain.WriteValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

// 시나리오
// id 값은 반드시 숫자, 비어있으면 안됨
// name 값은 반드시 입력, 비어있으면 안됨.


@Controller
public class WriteController {

    @RequestMapping("/write")
    public void wrtie() {
    }

    @RequestMapping("/writeOk")
    @ResponseBody
    public String writeOk(@Valid Write write,
                          BindingResult result) {
        System.out.println("writeOk() : " + write);
//        System.out.println("에러 개수: "+result.getErrorCount());   // 바인딩 과정에 발생된 에러의 개수

        showErrors(result);

        return """
                id: %d <br>
                name: %s <br>
                subject: %s <br>
                <button onclick="history.back()">돌아가기</button>
                """.formatted(write.getId(), write.getName(), write.getSubject());
    }

    // 바인딩 에러 출력 도우미 메소드
    private void showErrors(Errors errors) {
        if (errors.hasErrors()) {
            System.out.println("에러 개수: " + errors.getErrorCount());
            // Error = field + code 쌍으로 되어 있다.
            System.out.println("\t[field]\t|[code]");
            List<FieldError> errorList = errors.getFieldErrors();
            for (FieldError err : errorList) {
                System.out.println("\t" + err.getField() + "\t|" + err.getCode());
            }
        } else {
            System.out.println("에러 없음");
        }
    }

    // 이 컨트롤러 클래스의  handler 에서 폼 데이터를 바인딩 할 때 검증하는 Validator 객체 지정
    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.setValidator(new WriteValidator());
    }

}   // end controller

