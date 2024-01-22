package vladislavmaltsev.terranotabot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import vladislavmaltsev.terranotabot.enity.UserParameters;
import vladislavmaltsev.terranotabot.imagegeneration.ImageGenerator;
import vladislavmaltsev.terranotabot.mapgeneration.MapGenerator;
import vladislavmaltsev.terranotabot.mapgeneration.map.TerraNotaMap;

import java.io.IOException;
import java.io.InputStream;

@Service
public class BotContent {
    private final MapGenerator mapGenerator;
    private final ImageGenerator imageGenerator;
    private TerraNotaMap terraNotaMap;
    public BotContent(MapGenerator mapGenerator, ImageGenerator imageGenerator) {
        this.mapGenerator = mapGenerator;
        this.imageGenerator = imageGenerator;
    }

    public SendPhoto createSendPhotoTerraNotaMapImage(long chatId, UserParameters up) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        terraNotaMap = mapGenerator.generateMap(up.getMapSize(), up.getMapSize(),
                up.getScale(), up.getHeightDifference(), up.getIslandsModifier());
        InputStream terraImageIS = imageGenerator.generateImage(terraNotaMap);
        sendPhoto.setPhoto(new InputFile(terraImageIS, "myName"));
        try {
            terraImageIS.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sendPhoto;
    }
    public TerraNotaMap changeMapHeights(TerraNotaMap terraNotaMap, int value){
        int[][] array = terraNotaMap.getMapHeights().getArrayHeights();
        for(int x = 0; x < array.length; x++){
            for (int y = 0; y < array[x].length; y++){
                array[x][y] = array[x][y] + value;
            }
        }
        terraNotaMap.getMapHeights().setArrayHeights(array);
        return terraNotaMap;
    }
    public SendPhoto getExistedPhoto(long chatId, TerraNotaMap terraNotaMap, UserParameters userParameters){
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
//        TerraNotaMap terraNotaMap1 = TerraNotaMap.builder()
//                .width(userParameters.getMapSize())
//                .height(userParameters.getMapSize())
//                .mapHeights(mapHeights)
//                .build();
        InputStream terraImageIS = imageGenerator.generateImage(terraNotaMap);
        sendPhoto.setPhoto(new InputFile(terraImageIS, "myName"));
        try {
            terraImageIS.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sendPhoto;
    }


    public TerraNotaMap getTerraNotaMap() {
        return terraNotaMap;
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
