package vladislavmaltsev.terranotabot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import vladislavmaltsev.terranotabot.config.TelegramBotConfig;

@Component
@Slf4j
public class TelegramBotLongPolling extends TelegramLongPollingBot {
    private final TelegramBotConfig telegramBotConfig;

    @Autowired
    public TelegramBotLongPolling(TelegramBotConfig telegramBotConfig) {
        super(telegramBotConfig.getToken());
        this.telegramBotConfig = telegramBotConfig;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            System.out.println(update.getMessage().getText());
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(update.getMessage().getChatId());
            sendMessage.setText("This is text");
            log.error(update.getMessage().getText());
            try {
                execute(sendMessage);
            }catch (Exception e){

            }
        }
    }

    @Override
    public String getBotUsername() {
        return telegramBotConfig.getBotName();
    }
}
