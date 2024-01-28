package vladislavmaltsev.terranotabot.mapgeneration.map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import vladislavmaltsev.terranotabot.mapgeneration.map.generation.Alignment;
import vladislavmaltsev.terranotabot.mapgeneration.map.generation.HeightMap;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TerraNotaMap {
    private int width;
    private int height;
    private int mapScale;
    private MapHeights mapHeights;
    private int heightDifference;
    private int islandsModifier;

    public void generate() {
        double[][] heightMap = new double[height][width];
        Alignment alignment = new Alignment();

        new HeightMap().generateHeightMap(width - 1, heightMap, heightDifference, islandsModifier);

        if (mapScale > 1)
            alignment.alignmentMethod(heightMap, width - 1, mapScale);
        mapHeights = new MapHeights();
        mapHeights.setArrayHeights(new int[height][width]);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                mapHeights.getArrayHeights()[x][y] = (int)heightMap[x][y];
            }
        }

    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MapHeights {
//        @Id
        private String id;
        private int[][] arrayHeights;
    }
}
