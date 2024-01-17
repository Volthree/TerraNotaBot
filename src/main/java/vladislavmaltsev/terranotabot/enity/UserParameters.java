package vladislavmaltsev.terranotabot.enity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserParameters {

    private int updateId;
    private int messageId;
    private long chatId;

    private int mapSize = 513;
    private int scale = 1;
    private double heightDifference = 3;
    private int islandsModifier = 10;



}
