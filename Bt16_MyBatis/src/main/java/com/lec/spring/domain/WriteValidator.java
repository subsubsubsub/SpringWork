package com.lec.spring.domain;


import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class WriteValidator implements Validator {

    // 이 Validator 가 제공된 class 의 인스턴스 (clazz) 의 유효성 검사 할 수 있나?
    @Override
    public boolean supports(Class<?> clazz) {
        System.out.println("supports(" + clazz.getName() + ")호출");
        boolean result = Write.class.isAssignableFrom(clazz);
        System.out.println("supports() 가능? : " + result);
        return result;
    }
    // 주어진 객체(target)에 유효성검사를 하고
    // 유효성검사에 오류가 있는 경우 주어진 객체에 이 오류들을 errors 에 등록 한다.
    // 아래 validate() 는 Spring 이 기본적인 binding이 수행한 이후에 호출된다.
    // 따라서, errors 에는 Spring이 수행한 기본적인 binding 에러 들이 이미 담겨 있고
    // target 에는 binding 이 완료한 커맨드 객체가 전달된다.


    // 컨트롤러 핸들러 매개변수에 @Valid 가 붙어 있어야 validate() 가 동작한다

    @Override
    public void validate(Object target, Errors errors) {
        System.out.println("validate() 호출");
        Write write = (Write) target;

        String name = write.getName();
        if (name == null || name.trim().isEmpty()) {
            errors.rejectValue("name", "name은 필수입니다.");
        }

        // id 값이 음수일때 검증
        if (write.getId() < 0) {
            errors.rejectValue("id", "id 값은 0 이상이어야 합니다.");
            write.setId(0);
        }
    }
}
