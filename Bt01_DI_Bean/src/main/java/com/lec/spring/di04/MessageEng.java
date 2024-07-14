package com.lec.spring.di04;

import com.lec.spring.beans.MessageBean;
import org.springframework.stereotype.Component;

@Component("ENG")   // 이름이 "ENG" 인 MessageBean 타입 bean 생성
public class MessageEng implements MessageBean {

    String msgEng = "Good Morning";

    public MessageEng(){
        System.out.println("MessagenEng() 생성");
    }
    @Override
    public void sayHello() {
        System.out.println(msgEng);
    }
}
