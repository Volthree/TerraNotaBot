package vladislavmaltsev.terranotabot.components.utilscomponents;

import org.springframework.stereotype.Component;
import vladislavmaltsev.terranotabot.components.interfaces.ReplyMarcupInt;
import vladislavmaltsev.terranotabot.dto.UserParametersDTO;
import vladislavmaltsev.terranotabot.service.BottonsService;
import vladislavmaltsev.terranotabot.service.ReplyMarkupService;
import vladislavmaltsev.terranotabot.service.UpdateService;
import vladislavmaltsev.terranotabot.service.UserParametersService;

@Component
public class backComponent implements ReplyMarcupInt {

    private final ReplyMarkupService replyMarkupService;

    private final UserParametersService userParametersService;
    private final UpdateService updateService;
    private final BottonsService bottonsService;

    public backComponent(ReplyMarkupService replyMarkupService,
                         UserParametersService userParametersService,
                         UpdateService updateService, BottonsService bottonsService) {
        this.replyMarkupService = replyMarkupService;
        this.userParametersService = userParametersService;
        this.updateService = updateService;
        this.bottonsService = bottonsService;
    }

    public void setReplyMarcup() {
        UserParametersDTO userParameters = userParametersService
                .getUserParametersDependsExisted(
                        updateService.getChatId(),
                        updateService.getUpdate(),
                        updateService.getMessageId());

        replyMarkupService.setReplyMarkupKeyboard(bottonsService.getMainButtons(userParameters));
    }


    public String getId() {
        return "back";
    }
}
