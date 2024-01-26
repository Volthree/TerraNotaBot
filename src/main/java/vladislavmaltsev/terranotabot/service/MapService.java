package vladislavmaltsev.terranotabot.service;

import org.springframework.stereotype.Service;
import vladislavmaltsev.terranotabot.mapgeneration.map.TerraNotaMap;

@Service
public class MapService {
    public TerraNotaMap changeMapHeights(TerraNotaMap terraNotaMap, int value) {
        int[][] array = terraNotaMap.getMapHeights().getArrayHeights();
        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[x].length; y++) {
                array[x][y] = array[x][y] + value;
            }
        }
        terraNotaMap.getMapHeights().setArrayHeights(array);
        return terraNotaMap;
    }
}
