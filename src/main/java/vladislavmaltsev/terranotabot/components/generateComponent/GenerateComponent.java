package vladislavmaltsev.terranotabot.components.generateComponent;

import org.springframework.stereotype.Component;
import vladislavmaltsev.terranotabot.components.interfaces.ReplyMarcupInt;
import vladislavmaltsev.terranotabot.dto.UserParametersDTO;
import vladislavmaltsev.terranotabot.mapgeneration.map.TerraNotaMap;
import vladislavmaltsev.terranotabot.repository.UserParametersRepository;
import vladislavmaltsev.terranotabot.service.*;

import java.util.Optional;

@Component
public class GenerateComponent implements ReplyMarcupInt {

    private final ReplyMarkupService replyMarkupService;

    private final UserParametersService userParametersService;
    private final UpdateService updateService;
    private final UserParametersRepository userParametersRepository;
    private final BotContentService botContentService;
    private final BottonsService bottonsService;
    private final PhotoService photoService;

    public GenerateComponent(ReplyMarkupService replyMarkupService,
                             UserParametersService userParametersService,
                             UpdateService updateService,
                             UserParametersRepository userParametersRepository,
                             BotContentService botContentService,
                             BottonsService bottonsService,
                             PhotoService photoService) {
        this.replyMarkupService = replyMarkupService;
        this.userParametersService = userParametersService;
        this.updateService = updateService;
        this.userParametersRepository = userParametersRepository;
        this.botContentService = botContentService;
        this.bottonsService = bottonsService;
        this.photoService = photoService;
    }

    public void setReplyMarcup() {
        UserParametersDTO userParameters = userParametersService
                .getUserParametersDependsExisted(
                        updateService.getChatId(),
                        updateService.getUpdate(),
                        updateService.getMessageId());

        var up = userParametersRepository.findByChatId(updateService.getChatId()).orElseThrow();
        photoService.setSendPhoto(botContentService.getSendPhoto(updateService.getChatId(), null, up));
        TerraNotaMap t = botContentService.getTerraNotaMap();
        updateService.setUsernameDateMapIdMapHash(up, updateService.getUpdate(), t);
        userParametersRepository.save(up);
        replyMarkupService.setReplyMarkupKeyboard(bottonsService.getLastMapButton(Optional.of(up)));
    }


    public String getId() {
        return "Generate";
    }
}
