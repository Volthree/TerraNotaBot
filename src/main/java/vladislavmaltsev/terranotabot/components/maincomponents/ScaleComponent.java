package vladislavmaltsev.terranotabot.components.maincomponents;

import org.springframework.stereotype.Component;
import vladislavmaltsev.terranotabot.components.interfaces.ReplyMarcupInt;
import vladislavmaltsev.terranotabot.service.BottonsService;
import vladislavmaltsev.terranotabot.service.ReplyMarkupService;

@Component
public class ScaleComponent implements ReplyMarcupInt {

    private final ReplyMarkupService replyMarkupService;
    private final BottonsService bottonsService;

    public ScaleComponent(ReplyMarkupService replyMarkupService, BottonsService bottonsService) {
        this.replyMarkupService = replyMarkupService;
        this.bottonsService = bottonsService;
    }

    public void setReplyMarcup() {
        replyMarkupService.setReplyMarkupKeyboard(bottonsService.getScaleButtons());
    }


    public String getId() {
        return "Scale";
    }
}
