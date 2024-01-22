package vladislavmaltsev.terranotabot.mapgeneration.map.generation;

import vladislavmaltsev.terranotabot.mapgeneration.map.generation.Diamond;
import vladislavmaltsev.terranotabot.mapgeneration.map.generation.Square;

public class HeightMap {
    public void generateHeightMap(int width, double[][] heightMap,
                                  int heightDifference, int islandsModifier) {
        double score = 1;
        double step = width / score;
        double dX;
        double dY;
        double baseX;
        double baseY;
        double stepX;
        double stepY;
        heightMap[0][0] = Math.random() * 10;
        heightMap[width][0] = Math.random() * 10;
        heightMap[0][width] = Math.random() * 10;
        heightMap[width][width] = Math.random() * 10;

        for (int num = 1; num < 13; num++) {
            if (score == 1) {
                baseX = (width / (score * 2));
                baseY = (width / (score * 2));
                if (0 <= baseY && baseY <= width && 0 <= baseX && baseX <= width) {
                    score = 2;
                    step = width / score;
                    Square center = new Square();
                    center.go(score, heightMap, baseX, baseY, width, islandsModifier, heightDifference);
                }

                getDiamond(width, heightMap, heightDifference, score, step, baseX, baseY);

            } else {
                baseX = (width / (score * 2));
                dX = baseX;
                baseY = (width / (score * 2));

                stepX = 0;
                for (int num1 = 1; num1 <= score; num1++) {
                    dY = baseY;
                    dX = dX + stepX;
                    stepY = 0;
                    for (int num2 = 1; num2 <= score; num2++) {
                        if (0 <= dY && dY <= width && 0 <= dX && dX <= width) {
                            dY = dY + stepY;
                            Square sq = new Square();
                            sq.go((score * 2), heightMap, dX, dY, width, islandsModifier, heightDifference);
                            stepY = width / score;
                        }
                    }
                    stepX = width / score;
                }
                baseX = (width / (score * 2));
                dX = baseX;
                baseY = (width / (score * 2));

                stepX = 0;
                for (int num1 = 1; num1 <= score; num1++) {
                    step = width / (score * 2);
                    dY = baseY;
                    dX = dX + stepX;
                    stepY = 0;
                    for (int num2 = 1; num2 <= score; num2++) {
                        if (0 <= dY && dY <= width && 0 <= dX && dX <= width) {
                            dY = dY + stepY;
                            getDiamond(width, heightMap, heightDifference, score, step, dX, dY);
                            stepY = width / score;
                        }
                    }
                    stepX = width / score;
                }
                score = (score * 2);
            }

        }
    }

    private void getDiamond(int width, double[][] heightMap, int heightDifference, double score, double step, double xCordStart, double yCordStart) {
        new Diamond().diamond(step, heightMap, (xCordStart - step), yCordStart, width, score, heightDifference);
        new Diamond().diamond(step, heightMap, (xCordStart + step), yCordStart, width, score, heightDifference);
        new Diamond().diamond(step, heightMap, xCordStart, (yCordStart - step), width, score, heightDifference);
        new Diamond().diamond(step, heightMap, xCordStart, (yCordStart + step), width, score, heightDifference);
    }

}