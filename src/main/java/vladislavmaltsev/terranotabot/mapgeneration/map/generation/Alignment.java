package vladislavmaltsev.terranotabot.mapgeneration.map.generation;


public class Alignment {
    public void alignmentMethod(double[][] array, double poleSize, int mapScale) {
        int cellSize = mapScale;
        double ali = 0;
        int count = -1;
        for (int ih = cellSize / 2; ih <= poleSize; ih = ih + cellSize) {
            for (int iw = cellSize / 2; iw <= poleSize; iw = iw + cellSize) {
                for (int eightW = -cellSize / 2; eightW <= cellSize / 2; eightW++) {
                    for (int eightH = -cellSize / 2; eightH <= cellSize / 2; eightH++) {
                        if (ih + eightH >= 0 && ih + eightH <= poleSize && iw + eightW >= 0 && iw + eightW <= poleSize) {
                            ali = ali + array[iw + eightW][ih + eightH];
                            count = count + 1;
                        }
                    }
                }
                for (int eightW = -cellSize / 2; eightW <= cellSize / 2; eightW++) {
                    for (int eightH = -cellSize / 2; eightH <= cellSize / 2; eightH++) {
                        if (ih + eightH >= 0 && ih + eightH <= poleSize && iw + eightW >= 0 && iw + eightW <= poleSize) {
                            array[iw + eightW][ih + eightH] = Math.round(ali / count);
                        }
                    }
                }
                ali = 0;
                count = -1;
            }
        }
//        double minValue = array[0][0];
//        for (int wh = 0; wh <= poleSize - cellSize; wh = wh + cellSize) {
//            for (int hh = 0; hh <= poleSize - cellSize; hh = hh + cellSize) {
//                if (array[wh][hh] < minValue) {
//                    minValue = array[wh][hh];
//                }
//            }
//        }
//        System.out.println("Min value in Alignment " + minValue);
//        if (minValue < 0) {
//            minValue = Math.abs(minValue);
//            for (int wh = 0; wh <= poleSize - cellSize; wh = wh + cellSize) {
//                for (int hh = 0; hh <= poleSize - cellSize; hh = hh + cellSize) {
//                    array[wh][hh] = array[wh][hh] + minValue;
//                }
//            }
//        }
//        double maxValue = -99999;
//        for (int wh = 0; wh <= poleSize - cellSize; wh = wh + cellSize) {
//            for (int hh = 0; hh <= poleSize - cellSize; hh = hh + cellSize) {
//                if (array[wh][hh] > maxValue) {
//                    maxValue = array[wh][hh];
//                }
//            }
//        }
//        double coef = maxValue/13;
//        for (int wh = 0; wh <= poleSize - cellSize; wh = wh + cellSize) {
//            for (int hh = 0; hh <= poleSize - cellSize; hh = hh + cellSize) {
//                array[wh][hh] = array[wh][hh] * coef;
//            }
//        }
    }
}