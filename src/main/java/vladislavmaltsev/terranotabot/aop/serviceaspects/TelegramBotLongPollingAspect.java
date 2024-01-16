package vladislavmaltsev.terranotabot.aop.serviceaspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@Slf4j
public class TelegramBotLongPollingAspect {

//    @Pointcut("execution( * vladislavmaltsev.terranotabot.service.TestAspect.testAs(..))")
//    public void isTest(){}


//    @Before("isTest()")
//    public void testAspect(){
//        log.error("Test Aspect");
//        System.out.println("TestAspect");
//    }

//    @Pointcut("execution( *  vladislavmaltsev.terranotabot.service.TerraNotaBotLongPolling.onUpdateReceived(..))")
    @Pointcut("target(org.telegram.telegrambots.bots.TelegramLongPollingBot)")
    public void isOnUpdateReceived() {
    }

    @Before("isOnUpdateReceived()")
    public void logServiceBefore() {
        log.error("In onUpdateReceived method");
        System.out.println("IN ASPECT");
    }
}
