package vladislavmaltsev.terranotabot.components.interfaces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vladislavmaltsev.terranotabot.service.BottonsService;
import vladislavmaltsev.terranotabot.service.ReplyMarkupService;

@Component
public interface ReplyMarcupInt {


    public abstract void setReplyMarcup();

    public abstract String getId();
}
