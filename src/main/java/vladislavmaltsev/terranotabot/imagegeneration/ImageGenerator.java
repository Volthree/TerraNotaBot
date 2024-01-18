package vladislavmaltsev.terranotabot.imagegeneration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vladislavmaltsev.terranotabot.mapgeneration.map.TerraNotaMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import static vladislavmaltsev.terranotabot.util.colors.ColorDependsHeight.*;

@Slf4j
@Component
public class ImageGenerator {

    public InputStream generateImage(TerraNotaMap terraNotaMap) {

        BufferedImage image = new BufferedImage(terraNotaMap.getWidth(), terraNotaMap.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < terraNotaMap.getWidth(); x++) {
            for (int y = 0; y < terraNotaMap.getHeight(); y++) {
                image.setRGB(x, y, defineColor(terraNotaMap.getMapCells()[x][y].getHeight()).getRGB());
            }
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
