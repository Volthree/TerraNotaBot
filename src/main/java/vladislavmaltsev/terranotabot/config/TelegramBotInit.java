package vladislavmaltsev.terranotabot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import vladislavmaltsev.terranotabot.service.TerraNotaBotLongPolling;

@Component
@Slf4j
public class TelegramBotInit {


    TerraNotaBotLongPolling terraNotaBotLongPolling;

    @Autowired
    public TelegramBotInit(TerraNotaBotLongPolling terraNotaBotLongPolling) {
        this.terraNotaBotLongPolling = terraNotaBotLongPolling;
    }
    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        log.debug("Before register bot with DefaultBotSession.class");
        TelegramBotsApi telegramBotsApi;
        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(terraNotaBotLongPolling);
        } catch (TelegramApiException t) {
            log.error(t.getMessage());
        }
        log.debug("After register bot with DefaultBotSession.class");
    }
}
