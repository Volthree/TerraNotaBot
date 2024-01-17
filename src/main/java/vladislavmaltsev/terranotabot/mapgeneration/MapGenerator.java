package vladislavmaltsev.terranotabot.mapgeneration;

import vladislavmaltsev.terranotabot.mapgeneration.map.MapCell;
import vladislavmaltsev.terranotabot.mapgeneration.map.TerraNotaMap;

public class MapGenerator {

    public TerraNotaMap generateMap(int width, int height, int mapScale){
        var t = TerraNotaMap.builder()
                .width(width)
                .height(height)
                .mapScale(mapScale)
                .mapCells(new MapCell[width][height])
                .build();
        t.generate();
        return t;
    }
}
