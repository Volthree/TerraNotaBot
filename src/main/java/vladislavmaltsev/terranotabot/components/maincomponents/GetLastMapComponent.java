package vladislavmaltsev.terranotabot.components.maincomponents;

import org.springframework.stereotype.Component;
import vladislavmaltsev.terranotabot.components.interfaces.ReplyMarcupInt;
import vladislavmaltsev.terranotabot.repository.UserParametersRepository;
import vladislavmaltsev.terranotabot.service.BottonsService;
import vladislavmaltsev.terranotabot.service.ReplyMarkupService;
import vladislavmaltsev.terranotabot.service.UpdateService;

@Component
public class GetLastMapComponent implements ReplyMarcupInt {

    private final ReplyMarkupService replyMarkupService;
    private final BottonsService bottonsService;
    private final UserParametersRepository userParametersRepository;
    private final UpdateService updateService;
    public GetLastMapComponent(ReplyMarkupService replyMarkupService,
                               BottonsService bottonsService,
                               UserParametersRepository userParametersRepository,
                               UpdateService updateService) {
        this.replyMarkupService = replyMarkupService;
        this.bottonsService = bottonsService;
        this.userParametersRepository = userParametersRepository;
        this.updateService = updateService;
    }

    public void setReplyMarcup() {
        replyMarkupService.setReplyMarkupKeyboard(
                bottonsService.getLastMapButton(userParametersRepository.findByChatId(updateService.getChatId()))
        );
    }


    public String getId() {
        return "Get last map";
    }
}
