package vladislavmaltsev.terranotabot.components;

import jdk.jfr.DataAmount;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

@Component
@Data
@NoArgsConstructor
public class SendPhotoComponent {

    SendPhoto sendPhoto;
}
