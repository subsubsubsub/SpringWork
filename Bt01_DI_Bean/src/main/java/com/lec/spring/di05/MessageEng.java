package com.lec.spring.di05;

import com.lec.spring.beans.MessageBean;

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
