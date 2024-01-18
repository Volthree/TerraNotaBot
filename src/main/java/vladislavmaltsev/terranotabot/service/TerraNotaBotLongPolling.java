package vladislavmaltsev.terranotabot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import vladislavmaltsev.terranotabot.annotations.LogAnn;
import vladislavmaltsev.terranotabot.config.TelegramBotConfig;
import vladislavmaltsev.terranotabot.enity.UserParameters;
import vladislavmaltsev.terranotabot.service.enums.MainButtonsEnum;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TerraNotaBotLongPolling extends TelegramLongPollingBot {
    private final TelegramBotConfig telegramBotConfig;
    private final Bottons bottons;
    private final BotContent botContent;
    private final List<UserParameters> userParametersList = new ArrayList<>();

    @Autowired
    public TerraNotaBotLongPolling(TelegramBotConfig telegramBotConfig, Bottons bottons, BotContent botContent) {
        super(telegramBotConfig.getToken());
        this.telegramBotConfig = telegramBotConfig;
        this.bottons = bottons;
        this.botContent = botContent;
    }

    @Override
    @LogAnn
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = null;
        SendPhoto sendPhoto = null;
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();

            switch (message) {
                case "/start", "/menu" ->{
                    sendMessage = botContent.createSendMessage(update);
                    sendMessage.setReplyMarkup(bottons.getMainButtons());
                }

                case "/generate" -> sendPhoto = botContent.createSendPhoto(
                            update.getMessage().getChatId(),
                            713, 713, 2, 9999, 10);
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
            UserParameters userParameters;

            var i = userParametersList.stream().filter(x -> x.getMessageId() == messageId).findFirst();
            if (i.isPresent()) {
                log.info("Getting existed UserParameters");
                userParameters = i.get();
            } else {
                log.info("New UserParameters created");
                userParameters = UserParameters.getDefaultWithUpdate(update, messageId, chatId);
                userParametersList.add(userParameters);
            }
            String callbackData = update.getCallbackQuery().getData();
            MainButtonsEnum mainButton = MainButtonsEnum.valueOf(callbackData);
            log.info("Pressed button " + mainButton);
            EditMessageReplyMarkup replyMarkup = new EditMessageReplyMarkup();
            replyMarkup.setChatId(chatId);
            replyMarkup.setMessageId(messageId);

            switch (mainButton) {
                case SIZE -> replyMarkup.setReplyMarkup(bottons.getSizeButtons());
                case SCALE -> replyMarkup.setReplyMarkup(bottons.getScaleButtons());
                case HEIGHT_DIFFERENCE -> replyMarkup.setReplyMarkup(bottons.getHeightDifferenceButtons());
                case ISLANDS_MODIFIER -> replyMarkup.setReplyMarkup(bottons.getIslandsModifierButtons());
                case GET_LAST_MAP -> {}
                case GET_PREVIOUS_MAP -> {}
                case GENERATE -> {
                    sendPhoto = botContent.createSendPhoto(chatId,
                            userParameters.getMapSize(),
                            userParameters.getMapSize(),
                            userParameters.getScale(),
                            userParameters.getHeightDifference(),
                            userParameters.getIslandsModifier());
                    replyMarkup.setReplyMarkup(bottons.getSizeButtons());
                }
                case SMALL -> {
                    userParameters.setMapSize(129);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case MEDIUM -> {
                    userParameters.setMapSize(513);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case LARGE -> {
                    userParameters.setMapSize(713);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case X_1 -> {
                    userParameters.setScale(1);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case X_2 -> {
                    userParameters.setScale(2);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case X_4 -> {
                    userParameters.setScale(4);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case SMOOTH -> {
                    userParameters.setHeightDifference(2);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case HILL -> {
                    userParameters.setHeightDifference(4);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case MOUNTAIN -> {
                    userParameters.setHeightDifference(9);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case ISLANDS -> {
                    userParameters.setIslandsModifier(1);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case BLACKWATER -> {
                    userParameters.setIslandsModifier(10);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case CONTINENT -> {
                    userParameters.setIslandsModifier(40);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case BACK -> {
                    log.info(userParameters.toString());
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                default -> {}
            }
            try {
                if (sendPhoto != null) {
                    execute(sendPhoto);
                    log.warn("Photo sand");
                }
                execute(replyMarkup);
                log.warn("Reply markup sand");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }














    @Override
    public String getBotUsername() {
        return telegramBotConfig.getBotName();
    }
}
