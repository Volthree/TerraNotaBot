package vladislavmaltsev.terranotabot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import vladislavmaltsev.terranotabot.dto.UserParametersDTO;
import vladislavmaltsev.terranotabot.enity.UserParameters;
import vladislavmaltsev.terranotabot.repository.UserParametersRepository;
import vladislavmaltsev.terranotabot.util.MappingDTOAndClass;

import java.util.Optional;

import static vladislavmaltsev.terranotabot.util.MappingDTOAndClass.mapDTOAndClass;

@Service
@Slf4j
public class UserParametersService {

    private final UserParametersRepository userParametersRepository;
    private final BottonsService bottonsService;


    public UserParametersService(UserParametersRepository userParametersRepository,
                                 BottonsService bottonsService) {
        this.userParametersRepository = userParametersRepository;
        this.bottonsService = bottonsService;
    }

    @Transactional
    public UserParametersDTO getUserParametersDependsExisted(long chatId, Update update, int messageId) {
        var i = findByChatId(chatId);
        if (i.isPresent()) {
            log.info("Getting existed UserParameters");
            return i.orElseThrow();
        } else {
            log.info("New UserParameters created");
            var userParameters = MappingDTOAndClass.mapDTOAndClass(UserParameters.getDefaultWithUpdate(update, messageId, chatId),
                    UserParametersDTO.class);
            return save(userParameters).orElseThrow();
        }
    }
    public void mapSizeParameter(UserParametersDTO userParameters, int value, ReplyMarkupService replyMarkupService){
        userParameters.setMapSize(value);
        save(userParameters);
        replyMarkupService.setReplyMarkupKeyboard(bottonsService.getMainButtons(userParameters));
    }
    public void mapScaleParameter(UserParametersDTO userParameters, int value, ReplyMarkupService replyMarkupService){
        userParameters.setScale(value);
        save(userParameters);
        replyMarkupService.setReplyMarkupKeyboard(bottonsService.getMainButtons(userParameters));
    }
    public void mapHeightParameter(UserParametersDTO userParameters, int value, ReplyMarkupService replyMarkupService){
        userParameters.setHeightDifference(value);
        save(userParameters);
        replyMarkupService.setReplyMarkupKeyboard(bottonsService.getMainButtons(userParameters));
    }
    public void mapIslandParameter(UserParametersDTO userParameters, int value, ReplyMarkupService replyMarkupService){
        userParameters.setIslandsModifier(value);
        save(userParameters);
        replyMarkupService.setReplyMarkupKeyboard(bottonsService.getMainButtons(userParameters));
    }
    @Transactional(readOnly = true)
    public Optional<UserParametersDTO> findByChatId(long chatId){
        return Optional.ofNullable(
                mapDTOAndClass(userParametersRepository.findByChatId(chatId).orElse(null)
                        , UserParametersDTO.class));
    }
    public Optional<UserParametersDTO> save(UserParametersDTO userParametersDTO){
        return Optional.ofNullable(MappingDTOAndClass.mapDTOAndClass(userParametersRepository.save(
                MappingDTOAndClass.mapDTOAndClass(userParametersDTO, UserParameters.class)
        ), UserParametersDTO.class));
    }
}
