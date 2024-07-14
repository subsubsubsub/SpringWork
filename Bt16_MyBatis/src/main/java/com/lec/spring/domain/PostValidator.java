package com.lec.spring.domain;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


// Validator 인터페이스를 구형하여 Post 객체의 유효성을 검사, 웹 어플리케이션에서 사용자 입력을 검증하는데 사용되며, 잘못된 입력이 서버로 전달되지 않도록 한다.
public class PostValidator implements Validator {

    // Validator 가 제공된 클래스의 인스턴스를 검증할 수 있는지 여부를 결정
    @Override
    public boolean supports(Class<?> clazz) {
        boolean result = Post.class.isAssignableFrom(clazz);    // 주어진 클래스가 Post 클래스이거나 그 하위 클래스인지 확인한다.
        return result;
    }

    // target의 유효성을 검사  // Errors : 유효성 검사 오류를 저장 하는데 사용
    @Override
    public void validate(Object target, Errors errors) {
        Post post = (Post) target;
        System.out.println("Validate() 호출: " + post);

        String user = post.getUser();   // 작성 값 불러오긔
        if (user == null || user.trim().isEmpty()) {
            errors.rejectValue("user", "작성자는 필수입니다.");  // 특정 필드에 문제가 있을 때 그 문제를 설명하는 오류 메시지를 Errors 객체에 추가
        }

//        String subject = post.getSubject();
//        if (subject == null || subject.trim().isEmpty()) {
//            errors.rejectValue("subject", "글 제목은 필수입니다.");
//        }


        // ValidationUtils 사용
        // 단순히 빈(empty) 폼 데이터를 처리할때는 아래 와 같이 사용 가능
        // 두번째 매개변수 "subject" 은 반드시 target 클래스의 필드명 이어야 함
        // 게다가 Errors 에 등록될때도 동일한 field 명으로 등록된다.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "subject", "글 제목은 필수입니다.");
    }
}
