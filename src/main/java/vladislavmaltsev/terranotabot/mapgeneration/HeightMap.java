package vladislavmaltsev.terranotabot.mapgeneration;

public class HeightMap {
    public void heightMap(double poleSize, double[][] array) {
        double score = 1;
        double g = poleSize / score;
        double terra = 1;
        double xCord;
        double yCord;
        double xCordStart;
        double yCordStart;
        double coefficientStepX;
        double coefficientStepY;
        int heights = 1;
        array[0][0] = Math.random() * 10;
        array[(int) poleSize][0] = Math.random() * 10;
        array[0][(int) poleSize] = Math.random() * 10;
        array[(int) poleSize][(int) poleSize] = Math.random() * 10;

        for (int num = 1; num < 13; num++) {
            if (score == 1) {

                xCordStart = (poleSize / (score * 2));
                yCordStart = (poleSize / (score * 2));
                if (0 <= yCordStart && yCordStart <= poleSize && 0 <= xCordStart && xCordStart <= poleSize) {
                    score = 2;
                    g = poleSize / score;
                    Square center = new Square();
                    center.go(score, array, xCordStart, yCordStart, poleSize, heights, terra);
                }

                Diamond d1 = new Diamond();
                d1.diamond(g, array, (xCordStart - g), yCordStart, poleSize, score);
                Diamond d2 = new Diamond();
                d2.diamond(g, array, (xCordStart + g), yCordStart, poleSize, score);
                Diamond d3 = new Diamond();
                d3.diamond(g, array, xCordStart, (yCordStart - g), poleSize, score);
                Diamond d4 = new Diamond();
                d4.diamond(g, array, xCordStart, (yCordStart + g), poleSize, score);

            } else {
                xCordStart = (poleSize / (score * 2));
                xCord = xCordStart;
                yCordStart = (poleSize / (score * 2));

                coefficientStepX = 0;
                for (int num1 = 1; num1 <= score; num1++) {
                    yCord = yCordStart;
                    xCord = xCord + coefficientStepX;
                    coefficientStepY = 0;
                    for (int num2 = 1; num2 <= score; num2++) {
                        if (0 <= yCord && yCord <= poleSize && 0 <= xCord && xCord <= poleSize) {
                            yCord = yCord + coefficientStepY;
                            Square sq = new Square();
                            sq.go((score * 2), array, xCord, yCord, poleSize, heights, (terra));
                            coefficientStepY = poleSize / score;
                        }
                    }
                    coefficientStepX = poleSize / score;
                }
                xCordStart = (poleSize / (score * 2));
                xCord = xCordStart;
                yCordStart = (poleSize / (score * 2));

                coefficientStepX = 0;
                for (int num1 = 1; num1 <= score; num1++) {
                    g = poleSize / (score * 2);
                    yCord = yCordStart;
                    xCord = xCord + coefficientStepX;
                    coefficientStepY = 0;
                    for (int num2 = 1; num2 <= score; num2++) {
                        if (0 <= yCord && yCord <= poleSize && 0 <= xCord && xCord <= poleSize) {
                            yCord = yCord + coefficientStepY;
                            Diamond d1 = new Diamond();
                            d1.diamond(g, array, (xCord - g), yCord, poleSize, score);
                            Diamond d2 = new Diamond();
                            d2.diamond(g, array, (xCord + g), yCord, poleSize, score);
                            Diamond d3 = new Diamond();
                            d3.diamond(g, array, xCord, (yCord - g), poleSize, score);
                            Diamond d4 = new Diamond();
                            d4.diamond(g, array, xCord, (yCord + g), poleSize, score);
                            coefficientStepY = poleSize / score;
                        }
                    }
                    coefficientStepX = poleSize / score;
                }
                score = (score * 2);
                terra = terra + 3;
            }

        }
    }

}