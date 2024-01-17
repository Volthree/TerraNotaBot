package vladislavmaltsev.terranotabot.mapgeneration.map;

import lombok.*;
import vladislavmaltsev.terranotabot.mapgeneration.Alignment;
import vladislavmaltsev.terranotabot.mapgeneration.HeightMap;


@Data
@Builder
public class TerraNotaMap {


    private int width;
    private int height;
    private int mapScale;
    private MapCell[][] mapCells;

    public void generate(){
        HeightMap heightMap = new HeightMap();
        double[][] doubles = new double[height][width];
        Alignment alignment = new Alignment();

        heightMap.heightMap(712, doubles);
        alignment.alignmentMethod(doubles, 712);

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                mapCells[x][y] = new MapCell((int) doubles[x][y]);
            }
        }

    }
}
