package vladislavmaltsev.terranotabot.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import vladislavmaltsev.terranotabot.dto.UserParametersDTO;

@Service
@Data
public class SendMessageService {

    private final BotContentService botContentService;
    private final BottonsService bottonsService;
    private SendMessage sendMessage;

    public SendMessageService(BotContentService botContentService, BottonsService bottonsService) {
        this.botContentService = botContentService;
        this.bottonsService = bottonsService;
    }
    public SendMessage getDefaultMessage(Update update, UserParametersDTO userParametersDTO){
        sendMessage = botContentService.getSendMessage(update);
        sendMessage.setReplyMarkup(bottonsService.getMainButtons(userParametersDTO));
        return sendMessage;
    }

}
