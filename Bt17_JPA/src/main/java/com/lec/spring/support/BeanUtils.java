package com.lec.spring.support;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

// ApplicationContextAware 를 implement 하면
// ApplicationContext (IoC 컨테이너)를 가져올수 있다.
// 다른 클래스나 컴포넌트에서 @Autowired 없이도 빈을 가져올 수 있다.
//




// ApplicationContextAware 를 implement 하면
// ApplicationContext (IoC 컨테이너)를 가져올수 있다.

// Entity method 에서는 빈 주입이 안되기 때문에
//  해당 클래스를 사용하여 스프링 컨텍스트로부터 필요한 빈을 직접 가져와서 사용할 수 있다.

@Component
public class BeanUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        BeanUtils.applicationContext = applicationContext;
    }

    // 해당 T 클래스에 맞는 Bean 을 ApplicationContext 에서 받아오는 메소드 작성
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

}
