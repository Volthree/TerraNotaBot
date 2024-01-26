package vladislavmaltsev.terranotabot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class UpdateService {

    private Update update;
    public void setUpdate(Update update){
        this.update = update;
    }
    public int getMessageId(){
       return update.getCallbackQuery().getMessage().getMessageId();
    }
    public long getChatId(){
        return update.getCallbackQuery().getMessage().getChatId();
    }
    public String getCallbackQuery(){
        return update.getCallbackQuery().getData();
    }
}
