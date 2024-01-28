package vladislavmaltsev.terranotabot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import vladislavmaltsev.terranotabot.enity.UserParameters;
import vladislavmaltsev.terranotabot.mapgeneration.map.TerraNotaMap;

import java.time.LocalDateTime;

import static vladislavmaltsev.terranotabot.util.JsonData.JsonParser.toJson;

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
    public void setUsernameDateMapIdMapHash(UserParameters up, Update update, TerraNotaMap t) {
        up.setUsername(update.getCallbackQuery().getFrom().getUserName());
        up.setLocalDateTime(LocalDateTime.now());
        up.setMapHash(t.hashCode());
        up.setMapid(toJson(t));
    }
}
