package vladislavmaltsev.terranotabot.components.sizecomponents;

import org.springframework.stereotype.Component;
import vladislavmaltsev.terranotabot.components.interfaces.ReplyMarcupInt;
import vladislavmaltsev.terranotabot.dto.UserParametersDTO;
import vladislavmaltsev.terranotabot.service.ReplyMarkupService;
import vladislavmaltsev.terranotabot.service.UpdateService;
import vladislavmaltsev.terranotabot.service.UserParametersService;

@Component
public class MediumComponent implements ReplyMarcupInt {

    private final ReplyMarkupService replyMarkupService;

    private final UserParametersService userParametersService;
    private final UpdateService updateService;

    public MediumComponent(ReplyMarkupService replyMarkupService,
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

        userParametersService.mapSizeParameter(
                userParameters,
                513,
                replyMarkupService);
    }


    public String getId() {
        return "Medium";
    }
}
