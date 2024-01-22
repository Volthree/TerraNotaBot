package vladislavmaltsev.terranotabot.mapgeneration;

import org.springframework.stereotype.Component;
import vladislavmaltsev.terranotabot.mapgeneration.map.TerraNotaMap;

@Component
public class MapGenerator {

    public TerraNotaMap generateMap(int width, int height, int mapScale,
                                    int heightDifference, int islandsModifier){
        var t = TerraNotaMap.builder()
                .width(width)
                .height(height)
                .mapScale(mapScale)
                .heightDifference(heightDifference)
                .islandsModifier(islandsModifier)
                .build();
        t.generate();
        return t;
    }
}
