package vladislavmaltsev.terranotabot.components.maincomponents;

import org.springframework.stereotype.Component;
import vladislavmaltsev.terranotabot.components.interfaces.ReplyMarcupInt;
import vladislavmaltsev.terranotabot.service.BottonsService;
import vladislavmaltsev.terranotabot.service.ReplyMarkupService;

@Component
public class IslandsModifierComponent implements ReplyMarcupInt {

    private final ReplyMarkupService replyMarkupService;
    private final BottonsService bottonsService;

    public IslandsModifierComponent(ReplyMarkupService replyMarkupService, BottonsService bottonsService) {
        this.replyMarkupService = replyMarkupService;
        this.bottonsService = bottonsService;
    }

    public void setReplyMarcup(){
        replyMarkupService.setReplyMarkupKeyboard(bottonsService.getIslandsModifierButtons());
    }


    public String getId() {
        return "Islands modifier";
    }
}
