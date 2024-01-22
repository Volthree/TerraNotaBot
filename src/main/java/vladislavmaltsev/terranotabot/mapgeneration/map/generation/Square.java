package vladislavmaltsev.terranotabot.mapgeneration.map.generation;

public class Square {
    public void go(double score, double[][] heightMap, double baseX, double baseY,
                   int fieldSize, int islandModifier, int heightDifference) {
        double step = fieldSize / score;
        double heightTemp;
        double xcPlus = baseX + step;
        double xcMinus = baseX - step;
        double ycPlus = baseY + step;
        double ycMinus = baseY - step;

        double rb = heightMap[(int) xcPlus][(int) ycPlus];
        double rt = heightMap[(int) xcPlus][(int) ycMinus];
        double lt = heightMap[(int) xcMinus][(int) ycMinus];
        double lb = heightMap[(int) xcMinus][(int) ycPlus];

        if (heightMap[(int) baseX][(int) baseY] == 0) {
            heightTemp = ((rb + rt + lt + lb) / 4) + Math.random() * islandModifier / score;
            heightMap[(int) baseX][(int) baseY] = heightTemp;
        }
    }
}