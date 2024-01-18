package vladislavmaltsev.terranotabot.mapgeneration.map;

import lombok.Builder;
import lombok.Data;
import vladislavmaltsev.terranotabot.mapgeneration.Alignment;
import vladislavmaltsev.terranotabot.mapgeneration.HeightMap;


@Data
@Builder
public class TerraNotaMap {


    private int width;
    private int height;
    private int mapScale;
    private MapCell[][] mapCells;
    private double heightDifference;
    private int islandsModifier;

    public void generate() {
        double[][] heightMap = new double[height][width];
        Alignment alignment = new Alignment();

        new HeightMap().generateHeightMap(width - 1, heightMap, heightDifference, islandsModifier);

        if (mapScale > 1)
            alignment.alignmentMethod(heightMap, width - 1, mapScale);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                mapCells[x][y] = new MapCell((int) heightMap[x][y]);
            }
        }

    }
}
