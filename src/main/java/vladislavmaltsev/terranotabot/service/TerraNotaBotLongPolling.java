package vladislavmaltsev.terranotabot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import vladislavmaltsev.terranotabot.annotations.LogAnn;
import vladislavmaltsev.terranotabot.config.TelegramBotConfig;

@Service
@Slf4j
public class TerraNotaBotLongPolling extends TelegramLongPollingBot {
    private final TelegramBotConfig telegramBotConfig;
    private final TestAspect testAspect;
    @Autowired
    public TerraNotaBotLongPolling(TelegramBotConfig telegramBotConfig, TestAspect testAspect) {
        super(telegramBotConfig.getToken());
        this.telegramBotConfig = telegramBotConfig;
        this.testAspect = testAspect;
    }


    @Override
    @LogAnn
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(update.getMessage().getChatId());
            sendMessage.setText("This is text " +  update.getMessage().getText());
            testAspect.testAs();
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
