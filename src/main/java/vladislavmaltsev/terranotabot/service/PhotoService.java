package vladislavmaltsev.terranotabot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class PhotoService {

    public SendPhoto createSendPhoto(long chatId){
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        return sendPhoto;
    }

    public void closeImageStream(InputStream inputStream){
        try {
            inputStream.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
