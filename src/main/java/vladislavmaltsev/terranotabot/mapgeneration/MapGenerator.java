package vladislavmaltsev.terranotabot.mapgeneration;

import org.springframework.stereotype.Component;
import vladislavmaltsev.terranotabot.mapgeneration.map.MapCell;
import vladislavmaltsev.terranotabot.mapgeneration.map.TerraNotaMap;

@Component
public class MapGenerator {

    public TerraNotaMap generateMap(int width, int height, int mapScale,
                                    double heightDifference, int islandsModifier){
        var t = TerraNotaMap.builder()
                .width(width)
                .height(height)
                .mapScale(mapScale)
                .heightDifference(heightDifference)
                .islandsModifier(islandsModifier)
                .mapCells(new MapCell[width][height])
                .build();
        t.generate();
        return t;
    }
}
