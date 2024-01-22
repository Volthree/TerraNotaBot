package vladislavmaltsev.terranotabot.mapgeneration.map.generation;

public class Diamond {
    public void diamond(double step, double[][] heightMap, double xStartDia, double yStartDia,
                        int fieldSize, double score, int heightDiff) {
        if(heightMap[(int) xStartDia][(int) yStartDia] ==0) {

            double hDia;
            double xcPlusDia = xStartDia + step;
            double xcMinusDia = xStartDia - step;
            double ycPlusDia = yStartDia + step;
            double ycMinusDia = yStartDia - step;

            if(xcPlusDia > fieldSize){xcPlusDia = xStartDia - step;}
            if(xcMinusDia < 0){xcMinusDia =xStartDia + step;}
            if(ycPlusDia > fieldSize){ycPlusDia = yStartDia - step;}
            if(ycMinusDia < 0){ycMinusDia = yStartDia + step;}

            double rb = heightMap[(int) xcPlusDia][(int) yStartDia];
            double rt = heightMap[(int) xStartDia][(int) ycMinusDia];
            double lt = heightMap[(int) xStartDia][(int) ycPlusDia];
            double lb = heightMap[(int) xcMinusDia][(int) yStartDia];
            hDia = ((rb+lt+rt+lb)/4)+ ((Math.random()*13)-7 )/score*heightDiff;
            heightMap[(int) xStartDia][(int) yStartDia] = hDia;
        }
    }
}