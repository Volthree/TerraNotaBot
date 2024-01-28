package vladislavmaltsev.terranotabot.dto;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserParametersDTO {
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

    public String getMapSizeInString(){
        return switch (mapSize){
            case 129 -> "(Small)";
            case 513 -> "(Medium)";
            case 713 -> "(Large)";
            default -> "";
        };
    }
    public String getScaleInString(){
        return switch (scale){
            case 1 -> "(x1)";
            case 2 -> "(x2)";
            case 4 -> "(x4)";
            default -> "";
        };
    }
    public String getHeightDifferenceInString(){
        return switch (heightDifference){
            case 2 -> "(Smooth)";
            case 4 -> "(Hill)";
            case 9 -> "(Mountain)";
            default -> "";
        };
    }
    public String getIslandModifierInString(){
        return switch (islandsModifier){
            case 1 -> "(Islands)";
            case 10 -> "(Backwater)";
            case 40 -> "(Continent)";
            default -> "";
        };
    }
}
