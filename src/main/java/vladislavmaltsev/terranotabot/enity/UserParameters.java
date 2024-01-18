package vladislavmaltsev.terranotabot.enity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserParameters {

    private int updateId;
    private int messageId;
    private long chatId;
    private int mapSize;
    private int scale;
    private double heightDifference;
    private int islandsModifier;

    public static UserParameters getDefaultWithUpdate(Update update, int messageId, long chatId){
        return UserParameters.builder()
                .updateId(update.getUpdateId())
                .messageId(messageId)
                .chatId(chatId)
                .heightDifference(3)
                .mapSize(513)
                .scale(1)
                .islandsModifier(10)
                .build();
    }
}
