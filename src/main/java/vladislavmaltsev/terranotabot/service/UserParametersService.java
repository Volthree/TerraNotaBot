package vladislavmaltsev.terranotabot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import vladislavmaltsev.terranotabot.enity.UserParameters;
import vladislavmaltsev.terranotabot.repository.UserParametersRepository;

@Service
@Slf4j
public class UserParametersService {

    private final UserParametersRepository userParametersRepository;
    private final ReplyMarkupService replyMarkupService;
    private final BottonsService bottonsService;


    public UserParametersService(UserParametersRepository userParametersRepository,
                                 ReplyMarkupService replyMarkupService,
                                 BottonsService bottonsService) {
        this.userParametersRepository = userParametersRepository;
        this.replyMarkupService = replyMarkupService;
        this.bottonsService = bottonsService;
    }

    public UserParameters getUserParametersDependsExisted(long chatId, Update update, int messageId) {
        var i = userParametersRepository.findByChatId(chatId);
        if (i.isPresent()) {
            log.info("Getting existed UserParameters");
            return i.get();
        } else {
            log.info("New UserParameters created");
            UserParameters userParameters = UserParameters.getDefaultWithUpdate(update, messageId, chatId);
            userParametersRepository.save(userParameters);
            return userParameters;
        }
    }
    public void mapSizeParameter(UserParameters userParameters, int value, ReplyMarkupService replyMarkupService){
        userParameters.setMapSize(value);
        userParametersRepository.save(userParameters);
        replyMarkupService.setReplyMarkupKeyboard(bottonsService.getMainButtons(userParameters));
    }
    public void mapScaleParameter(UserParameters userParameters, int value, ReplyMarkupService replyMarkupService){
        userParameters.setScale(value);
        userParametersRepository.save(userParameters);
        replyMarkupService.setReplyMarkupKeyboard(bottonsService.getMainButtons(userParameters));
    }
    public void mapHeightParameter(UserParameters userParameters, int value, ReplyMarkupService replyMarkupService){
        userParameters.setHeightDifference(value);
        userParametersRepository.save(userParameters);
        replyMarkupService.setReplyMarkupKeyboard(bottonsService.getMainButtons(userParameters));
    }
    public void mapIslandParameter(UserParameters userParameters, int value, ReplyMarkupService replyMarkupService){
        userParameters.setIslandsModifier(value);
        userParametersRepository.save(userParameters);
        replyMarkupService.setReplyMarkupKeyboard(bottonsService.getMainButtons(userParameters));
    }
}
