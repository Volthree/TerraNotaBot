package vladislavmaltsev.terranotabot.mapgeneration;

public class Diamond {
    public void diamond(double g, double[][] array, double xStartDia, double yStartDia, double fieldSize, double score) {
        if(array[(int) xStartDia][(int) yStartDia] ==0) {

            double hDia;
            double xcPlusDia = xStartDia + g;
            double xcMinusDia = xStartDia - g;
            double ycPlusDia = yStartDia + g;
            double ycMinusDia = yStartDia - g;

            if(xcPlusDia > fieldSize){xcPlusDia = xStartDia - g;}
            if(xcMinusDia < 0){xcMinusDia =xStartDia + g;}
            if(ycPlusDia > fieldSize){ycPlusDia = yStartDia - g;}
            if(ycMinusDia < 0){ycMinusDia = yStartDia + g;}

            double rb = array[(int) xcPlusDia][(int) yStartDia];
            double rt = array[(int) xStartDia][(int) ycMinusDia];
            double lt = array[(int) xStartDia][(int) ycPlusDia];
            double lb = array[(int) xcMinusDia][(int) yStartDia];
            hDia = ((rb+lt+rt+lb)/4)+ ((Math.random()*10)-7 )/score*3;
            array[(int) xStartDia][(int) yStartDia] = hDia;
        }
    }
}