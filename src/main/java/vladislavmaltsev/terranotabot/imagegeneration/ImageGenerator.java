package vladislavmaltsev.terranotabot.imagegeneration;

import vladislavmaltsev.terranotabot.mapgeneration.map.TerraNotaMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageGenerator {


    public InputStream generateImage(TerraNotaMap terraNotaMap){

        BufferedImage image = new BufferedImage(terraNotaMap.getWidth(), terraNotaMap.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < terraNotaMap.getWidth(); x++) {
            for (int y = 0; y < terraNotaMap.getHeight(); y++) {
                image.setRGB(x, y, terraNotaMap.getMapCells()[x][y].getHeight()*100000);
            }
        }
        InputStream inputStream = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
             inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }


}
