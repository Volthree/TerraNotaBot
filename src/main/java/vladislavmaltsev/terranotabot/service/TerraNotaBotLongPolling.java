package vladislavmaltsev.terranotabot.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.cookie.SM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import vladislavmaltsev.terranotabot.annotations.LogAnn;
import vladislavmaltsev.terranotabot.config.TelegramBotConfig;
import vladislavmaltsev.terranotabot.imagegeneration.ImageGenerator;
import vladislavmaltsev.terranotabot.mapgeneration.MapGenerator;
import vladislavmaltsev.terranotabot.mapgeneration.map.TerraNotaMap;
import vladislavmaltsev.terranotabot.service.enums.MainButtons;

import java.io.InputStream;
import java.util.List;

import static vladislavmaltsev.terranotabot.service.enums.MainButtons.*;

@Service
@Slf4j
public class TerraNotaBotLongPolling extends TelegramLongPollingBot {
    private final TelegramBotConfig telegramBotConfig;

    @Autowired
    public TerraNotaBotLongPolling(TelegramBotConfig telegramBotConfig) {
        super(telegramBotConfig.getToken());
        this.telegramBotConfig = telegramBotConfig;
    }

    public static InlineKeyboardButton createButton(String setText, MainButtons button) {
        var i = new InlineKeyboardButton();
        i.setText(setText);
        i.setCallbackData(button.toString());
        return i;
    }

    @Override
    @LogAnn
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            SendMessage sendMessage = null;
            SendPhoto sendPhoto = null;
            switch (message) {
                case "/start" -> {
                }
                case "/generate" -> {

                    sendPhoto = createSendPhoto(update, 713, 713, 1);
                }
                case "/menu" -> {
                    sendMessage = createSendMessage(update);
                }
            }

            try {
                if (sendMessage != null)
                    execute(sendMessage);
                if (sendPhoto != null)
                    execute(sendPhoto);
            } catch (Exception e) {
                log.error("In first try 64 " + e.getMessage());
            }
        } else if (update.hasCallbackQuery()) {
            int messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            String callbackData = update.getCallbackQuery().getData();
            MainButtons mainButton = MainButtons.valueOf(callbackData);
            log.info("Pressed button " + mainButton);
            EditMessageReplyMarkup replyMarkup = new EditMessageReplyMarkup();
            replyMarkup.setChatId(chatId);
            replyMarkup.setMessageId(messageId);
            log.info("chatId-" + chatId + "  messageId-" + messageId);
            switch (mainButton) {
                case SIZE -> {
                    replyMarkup.setReplyMarkup(getSizeButtons());
                }
                case SCALE -> {
                    replyMarkup.setReplyMarkup(getScaleButtons());
                }
                case HEIGHT_DIFFERENCE -> {
                    replyMarkup.setReplyMarkup(getHeightDifferenceButtons());
                }
                case ISLANDS_MODIFIER -> {
                    replyMarkup.setReplyMarkup(getIslandsModifierButtons());
                }

                case GET_LAST_MAP -> {
                }
                case GET_PREVIOUS_MAP -> {
                }


                case GENERATE -> {
                }

                case SMALL -> {
                }
                case MEDIUM -> {
                }
                case LARGE -> {
                }

                case BACK -> {
                    replyMarkup.setReplyMarkup(getMainButtons());
                }
                default -> {
                }
            }
            try {
                execute(replyMarkup);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public SendMessage createSendMessage(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText("Choose generation properties");
        sendMessage.setReplyMarkup(getMainButtons());
        return sendMessage;
    }

    public InlineKeyboardMarkup getSizeButtons() {

        var row1Button = List.of(
                createButton("Small", SMALL),
                createButton("Medium", MEDIUM),
                createButton("Large", LARGE)
        );
        var row2Button = List.of(
                createButton("back", BACK)
        );
        var rowsButton = List.of(
                row1Button,
                row2Button
        );
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowsButton);
        return inlineKeyboardMarkup;
    }
    public InlineKeyboardMarkup getHeightDifferenceButtons() {

        var row1Button = List.of(
                createButton("Smooth", SMOOTH),
                createButton("Hill", HILL),
                createButton("Mountain", MOUNTAIN)
        );
        var row2Button = List.of(
                createButton("back", BACK)
        );
        var rowsButton = List.of(
                row1Button,
                row2Button
        );
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowsButton);
        return inlineKeyboardMarkup;
    }
    public InlineKeyboardMarkup getIslandsModifierButtons() {

        var row1Button = List.of(
                createButton("Islands", ISLANDS),
                createButton("Backwater", BLACKWATER),
                createButton("Continent", CONTINENT)
        );
        var row2Button = List.of(
                createButton("back", BACK)
        );
        var rowsButton = List.of(
                row1Button,
                row2Button
        );
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowsButton);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getScaleButtons() {

        var row1Button = List.of(
                createButton("x1", X_1),
                createButton("x2", X_2),
                createButton("x4", X_4)
        );
        var row2Button = List.of(
                createButton("back", BACK)
        );
        var rowsButton = List.of(
                row1Button,
                row2Button
        );
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowsButton);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getMainButtons() {

        var row1Button = List.of(
                createButton("Map size", SIZE),
                createButton("Scale", SCALE)
        );
        var row2Button = List.of(
                createButton("Height difference", HEIGHT_DIFFERENCE),
                createButton("Islands modifier", ISLANDS_MODIFIER)

        );
        var row3Button = List.of(
                createButton("Get last map", GET_LAST_MAP),
                createButton("Get previous map", GET_PREVIOUS_MAP)

        );
        var row4Button = List.of(
                createButton("Generate", GENERATE)

        );
        var rowsButton = List.of(
                row1Button,
                row2Button,
                row3Button,
                row4Button
        );
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowsButton);
        return inlineKeyboardMarkup;
    }

    public SendPhoto createSendPhoto(Update update, int width, int height, int mapScale) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(update.getMessage().getChatId());
        MapGenerator mapGenerator = new MapGenerator();
        TerraNotaMap terraNotaMap = mapGenerator.generateMap(width, height, mapScale);
        InputStream terraImageIS = new ImageGenerator().generateImage(terraNotaMap);
        sendPhoto.setPhoto(new InputFile(terraImageIS, "myName"));
        return sendPhoto;
    }


    @Override
    public String getBotUsername() {
        return telegramBotConfig.getBotName();
    }
}
