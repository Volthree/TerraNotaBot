package vladislavmaltsev.terranotabot.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import vladislavmaltsev.terranotabot.enity.UserParameters;
import vladislavmaltsev.terranotabot.imagegeneration.ImageGenerator;
import vladislavmaltsev.terranotabot.mapgeneration.MapGenerator;
import vladislavmaltsev.terranotabot.mapgeneration.map.TerraNotaMap;

import java.io.InputStream;

@Service
@Getter
public class BotContent {
    private final MapGenerator mapGenerator;
    private final ImageGenerator imageGenerator;
    private final PhotoService photoService;
    private TerraNotaMap terraNotaMap;

    public BotContent(MapGenerator mapGenerator,
                      ImageGenerator imageGenerator,
                      PhotoService photoService) {
        this.mapGenerator = mapGenerator;
        this.imageGenerator = imageGenerator;
        this.photoService = photoService;
    }
    public SendPhoto getSendPhoto(long chatId, TerraNotaMap terraNotaMap, UserParameters up) {
        SendPhoto sendPhoto = photoService.createSendPhoto(chatId);
        if (terraNotaMap == null) {
            terraNotaMap = mapGenerator.generateMap(up.getMapSize(), up.getMapSize(),
                    up.getScale(), up.getHeightDifference(), up.getIslandsModifier());
            this.terraNotaMap = terraNotaMap;
        }
        InputStream terraImageIS = imageGenerator.generateImage(terraNotaMap);
        sendPhoto.setPhoto(new InputFile(terraImageIS, Integer.toString(terraNotaMap.hashCode())));
        photoService.closeImageStream(terraImageIS);
        return sendPhoto;
    }
    public SendMessage createSendMessage(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText("""
                <strong>This is map generation bot!</strong>
                                
                <em>Choose parameters: </em>
                <i><b>Map size - </b> for picture size</i>
                <i><b>Scale - </b> for pixel-style</i>
                <i><b>Height difference - </b> for heights</i>
                <i><b>Islands modifier - </b> for land area</i>
                <i>and then <b>GENERATE</b> </i>
                <i>You can <b>Get last map</b> for changing water filling on last created map.</i>""");
        sendMessage.enableHtml(true);
        return sendMessage;
    }
}
