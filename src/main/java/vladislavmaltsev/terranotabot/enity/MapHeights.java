package vladislavmaltsev.terranotabot.enity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapHeights {

    @Id
    private String id;

    private int[][] arrayHeights;

}
