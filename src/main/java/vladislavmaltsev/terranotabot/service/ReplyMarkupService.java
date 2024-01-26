package vladislavmaltsev.terranotabot.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import vladislavmaltsev.terranotabot.enity.UserParameters;
import vladislavmaltsev.terranotabot.mapgeneration.map.TerraNotaMap;
import vladislavmaltsev.terranotabot.repository.UserParametersRepository;
import java.util.Objects;
import java.util.Optional;
import static vladislavmaltsev.terranotabot.util.JsonData.JsonParser.*;

@Service
@Data
public class ReplyMarkupService {

    private EditMessageReplyMarkup replyMarkup;
    private final UserParametersRepository userParametersRepository;
    private final MapService mapService;
    private final BottonsService bottonsService;
    private final BotContentService botContentService;

    public ReplyMarkupService(UserParametersRepository userParametersRepository,
                              MapService mapService,
                              BottonsService bottonsService,
                              BotContentService botContentService){
        this.userParametersRepository = userParametersRepository;
        this.mapService = mapService;
        this.bottonsService = bottonsService;
        this.botContentService = botContentService;
    }
    public void setReplyMarkup(long chatId, int messageId) {
        replyMarkup = new EditMessageReplyMarkup();
        replyMarkup.setChatId(chatId);
        replyMarkup.setMessageId(messageId);
    }
    public void setReplyMarkup(InlineKeyboardMarkup inlineKeyboardMarkup) {
        replyMarkup.setReplyMarkup(inlineKeyboardMarkup);
    }

    public void setReplyMarkupUpdateData(String callbackData, EditMessageReplyMarkup replyMar1kup,
                                          int value, String type, SendPhoto sendPhoto, long chatId) {
        var usersParamByMapId = userParametersRepository.findByMapHash(Integer.parseInt(callbackData));
        Optional<TerraNotaMap> terraNotaMap = usersParamByMapId.map(userParameters -> fromJson(userParameters.getMapid()));

        if (!Objects.equals(type, "get map ")) {
            TerraNotaMap changedMapHeights = mapService.changeMapHeights(terraNotaMap.orElse(null), value);
            usersParamByMapId.get().setMapid(toJson(terraNotaMap.get()));
            usersParamByMapId = Optional.of(userParametersRepository.save(usersParamByMapId.get()));
            replyMarkup.setReplyMarkup(bottonsService.getLastMapButton(usersParamByMapId));
        } else {
            sendPhoto = botContentService.getSendPhoto(chatId, terraNotaMap.orElse(null), null);
            Optional<UserParameters> userPatamLastMapOptional = userParametersRepository.findByChatId(chatId);
            replyMarkup.setReplyMarkup(bottonsService.getLastMapButton(userPatamLastMapOptional));
        }
    }
}
