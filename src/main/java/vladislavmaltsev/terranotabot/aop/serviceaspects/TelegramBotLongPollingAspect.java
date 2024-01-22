package vladislavmaltsev.terranotabot.aop.serviceaspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Aspect
@Configuration
@Slf4j
public class TelegramBotLongPollingAspect {

    @Pointcut("execution( *  org.telegram.telegrambots.meta.generics.LongPollingBot.onUpdatesReceived(java.util.List))")
    public void isOnUpdateReceivedList() {
    }

    @Before("isOnUpdateReceivedList() " +
            "&& args(object)")
    public void logServiceBefore(JoinPoint joinPoint, Object object) {

        List<Update> updateList = (List<Update>) object;
        for (Update update : updateList) {
            if (update.hasMessage())
                log.info("Update messages: {} in UpdateId {}", update.getMessage().getText(), update.getUpdateId());
            else
                log.info("Update without message in {}", update.getUpdateId());
        }
    }
}
