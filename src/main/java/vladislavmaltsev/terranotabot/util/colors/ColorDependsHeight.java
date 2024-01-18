package vladislavmaltsev.terranotabot.util.colors;

import java.awt.*;

public class ColorDependsHeight {
    public static Color defineColor(int height) {

        if (height > 13) return Color.white;
        return switch (height) {
            case 0 -> Color.BLUE.darker().darker();
            case 1 -> Color.BLUE.darker();
            case 2 -> Color.BLUE;
            case 3 -> Color.cyan.darker();
            case 4 -> new Color(35, 176, 204);
            case 5 -> Color.cyan.brighter();
            case 6 -> Color.orange.brighter();
            case 7 -> Color.green.darker();
            case 8 -> Color.green.darker().darker();
            case 9 -> new Color(40, 90, 90);
            case 10 -> new Color(50, 80, 80);
            case 11 -> Color.GRAY;
            case 12 -> Color.GRAY.brighter();
            case 13 -> Color.lightGray;
            default -> new Color(14, 18, 64);
        };
    }
}
