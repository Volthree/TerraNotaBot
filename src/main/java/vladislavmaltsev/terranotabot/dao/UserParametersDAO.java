package vladislavmaltsev.terranotabot.dao;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserParametersDAO {
    private int userParametersId;
    private int updateId;
    private int messageId;
    private long chatId;
    private int mapSize;
    private int scale;
    private int heightDifference;
    private int islandsModifier;
    private String username;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime localDateTime;
    private String mapid;
    private int mapHash;
}
