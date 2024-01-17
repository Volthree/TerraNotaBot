package vladislavmaltsev.terranotabot.mapgeneration;

public class Square {
    public void go(double score, double[][] array, double xCord, double yCord, double fieldSize, int v, double it) {
        double g = fieldSize / score;
        double heightTemp;
        double xcPlus = xCord + g;
        double xcMinus = xCord - g;
        double ycPlus = yCord + g;
        double ycMinus = yCord - g;

        double rb = array[(int) xcPlus][(int) ycPlus];
        double rt = array[(int) xcPlus][(int) ycMinus];
        double lt = array[(int) xcMinus][(int) ycMinus];
        double lb = array[(int) xcMinus][(int) ycPlus];

        if (array[(int) xCord][(int) yCord] == 0) {
            heightTemp = ((rb + rt + lt + lb) / 4) + Math.random() * 10 / score;
            array[(int) xCord][(int) yCord] = heightTemp;
        }
    }
}