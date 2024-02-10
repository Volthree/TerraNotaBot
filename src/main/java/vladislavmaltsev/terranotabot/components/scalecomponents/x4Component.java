package vladislavmaltsev.terranotabot.components.scalecomponents;

import org.springframework.stereotype.Component;
import vladislavmaltsev.terranotabot.components.interfaces.ReplyMarcupInt;
import vladislavmaltsev.terranotabot.dto.UserParametersDTO;
import vladislavmaltsev.terranotabot.service.ReplyMarkupService;
import vladislavmaltsev.terranotabot.service.UpdateService;
import vladislavmaltsev.terranotabot.service.UserParametersService;

@Component
public class x4Component implements ReplyMarcupInt {

    private final ReplyMarkupService replyMarkupService;

    private final UserParametersService userParametersService;
    private final UpdateService updateService;

    public x4Component(ReplyMarkupService replyMarkupService,
                       UserParametersService userParametersService,
                       UpdateService updateService) {
        this.replyMarkupService = replyMarkupService;
        this.userParametersService = userParametersService;
        this.updateService = updateService;
    }

    public void setReplyMarcup() {
        UserParametersDTO userParameters = userParametersService
                .getUserParametersDependsExisted(
                        updateService.getChatId(),
                        updateService.getUpdate(),
                        updateService.getMessageId());

        userParametersService.mapScaleParameter(
                userParameters, 4, replyMarkupService);
    }


    public String getId() {
        return "x4";
    }
}
