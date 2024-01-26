package vladislavmaltsev.terranotabot.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
@Data
public class PhotoService {

    private SendPhoto sendPhoto;

    public SendPhoto createSendPhoto(long chatId) {
        this.sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        return sendPhoto;
    }

    public void closeImageStream(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
