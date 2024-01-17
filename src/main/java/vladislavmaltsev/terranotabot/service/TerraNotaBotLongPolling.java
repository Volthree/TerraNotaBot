package vladislavmaltsev.terranotabot.service;

import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;
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
                log.error(e.getMessage());
            }
        } else if (update.hasCallbackQuery()) {
            int messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            String callbackData = update.getCallbackQuery().getData();
            MainButtons mainButton = MainButtons.valueOf(callbackData);
            log.info("Pressed button " + mainButton);
            EditMessageReplyMarkup replyMarkup = null;
            switch (mainButton){
                case SIZE -> {
                    System.out.println("size");
                    replyMarkup = new EditMessageReplyMarkup();
                    replyMarkup.setChatId(chatId);
                    replyMarkup.setMessageId(messageId);
                    replyMarkup.setReplyMarkup(getSizeButtons());
                }
                case SCALE -> {System.out.println("scale");}
                case GENERATE -> {System.out.println("generate");}
                case GET_LAST_MAP -> {System.out.println("getLsatMap");}
                case GET_PREVIOUS_MAP -> {System.out.println("GerPrevMap");}
                case ISLANDS_MODIFIER -> {System.out.println("getIslands");}
                case HEIGHT_DIFFERENCE -> {System.out.println("heightdiff");}

                case SMALL -> {}
                case MEDIUM -> {}
                case LARGE -> {}

                case BACK -> {}
                default -> {System.out.println("default");}
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
    public InlineKeyboardMarkup getSizeButtons(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsButton = new ArrayList<>();
        List<InlineKeyboardButton> row1Button = new ArrayList<>();
        List<InlineKeyboardButton> row2Button = new ArrayList<>();

        var smallButton = new InlineKeyboardButton();
        smallButton.setText("Small");
        smallButton.setCallbackData(SMALL.toString());

        var mediumButton = new InlineKeyboardButton();
        mediumButton.setText("Medium");
        mediumButton.setCallbackData(MEDIUM.toString());

        var largeButton = new InlineKeyboardButton();
        largeButton.setText("Medium");
        largeButton.setCallbackData(LARGE.toString());

        row1Button.add(smallButton);
        row1Button.add(mediumButton);
        row1Button.add(largeButton);

        var backButton = new InlineKeyboardButton();
        backButton.setText("back");
        backButton.setCallbackData(BACK.toString());
        row2Button.add(backButton);

        rowsButton.add(row1Button);
        rowsButton.add(row2Button);

        inlineKeyboardMarkup.setKeyboard(rowsButton);
        return inlineKeyboardMarkup;
    }
    public InlineKeyboardMarkup getMainButtons(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsButton = new ArrayList<>();
        List<InlineKeyboardButton> row1Button = new ArrayList<>();
        List<InlineKeyboardButton> row2Button = new ArrayList<>();
        List<InlineKeyboardButton> row3Button = new ArrayList<>();
        List<InlineKeyboardButton> row4Button = new ArrayList<>();

        var sizeButton = new InlineKeyboardButton();
        sizeButton.setText("Map size");
        sizeButton.setCallbackData(SIZE.toString());

        var cellSizeButton = new InlineKeyboardButton();
        cellSizeButton.setText("Scale");
        cellSizeButton.setCallbackData(MainButtons.SCALE.toString());

        row1Button.add(sizeButton);
        row1Button.add(cellSizeButton);

        var heightDifferenceButton = new InlineKeyboardButton();
        heightDifferenceButton.setText("Height difference");
        heightDifferenceButton.setCallbackData(MainButtons.HEIGHT_DIFFERENCE.toString());

        var islandsButton = new InlineKeyboardButton();
        islandsButton.setText("Islands modifier");
        islandsButton.setCallbackData(MainButtons.ISLANDS_MODIFIER.toString());

        row2Button.add(heightDifferenceButton);
        row2Button.add(islandsButton);

        var getLastGeneratedButton = new InlineKeyboardButton();
        getLastGeneratedButton.setText("Get last map");
        getLastGeneratedButton.setCallbackData(MainButtons.GET_LAST_MAP.toString());

        var getPreviousGeneratedButton = new InlineKeyboardButton();
        getPreviousGeneratedButton.setText("Get previous map");
        getPreviousGeneratedButton.setCallbackData(MainButtons.GET_PREVIOUS_MAP.toString());

        row3Button.add(getLastGeneratedButton);
        row3Button.add(getPreviousGeneratedButton);

        var generateButton = new InlineKeyboardButton();
        generateButton.setText("Generate");
        generateButton.setCallbackData(MainButtons.GENERATE.toString());

        row4Button.add(generateButton);

        rowsButton.add(row1Button);
        rowsButton.add(row2Button);
        rowsButton.add(row3Button);
        rowsButton.add(row4Button);

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
