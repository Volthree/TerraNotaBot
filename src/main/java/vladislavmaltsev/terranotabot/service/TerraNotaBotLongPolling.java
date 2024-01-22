package vladislavmaltsev.terranotabot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import vladislavmaltsev.terranotabot.annotations.LogAnn;
import vladislavmaltsev.terranotabot.config.TelegramBotConfig;
import vladislavmaltsev.terranotabot.enity.UserParameters;
import vladislavmaltsev.terranotabot.mapgeneration.map.TerraNotaMap;
import vladislavmaltsev.terranotabot.repository.MapHeightsRepository;
import vladislavmaltsev.terranotabot.repository.TerraNotaMapReporitory;
import vladislavmaltsev.terranotabot.repository.UserParametersRepository;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class TerraNotaBotLongPolling extends TelegramLongPollingBot {
    private final TelegramBotConfig telegramBotConfig;
    private final Bottons bottons;
    private final BotContent botContent;
    private final UserParametersRepository userParametersRepository;
    private final TerraNotaMapReporitory terraNotaMapReporitory;

    private final MapHeightsRepository mapHeightsRepository;

    @Autowired
    public TerraNotaBotLongPolling(TelegramBotConfig telegramBotConfig,
                                   Bottons bottons, BotContent botContent,
                                   UserParametersRepository userParametersRepository,
                                   TerraNotaMapReporitory terraNotaMapReporitory,
                                   MapHeightsRepository mapHeightsRepository) {
        super(telegramBotConfig.getToken());
        this.telegramBotConfig = telegramBotConfig;
        this.bottons = bottons;
        this.botContent = botContent;
        this.userParametersRepository = userParametersRepository;
        this.terraNotaMapReporitory = terraNotaMapReporitory;
        this.mapHeightsRepository = mapHeightsRepository;
    }

    @Override
    @LogAnn
    @Transactional
    public void onUpdateReceived(Update update) {
        SendPhoto sendPhoto = null;
        UserParameters userParameters;
        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (update.getMessage().getText()) {
                case "/start", "/menu" -> createSendMessage(update);
            }
        } else if (update.hasCallbackQuery()) {
            int messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            userParameters = getUserParameters(chatId, update, messageId);
            EditMessageReplyMarkup replyMarkup = getReplyMarkup(chatId, messageId);

            String callbackData = update.getCallbackQuery().getData();
            log.error("CallBackData " + callbackData);

            switch (callbackData) {
                case "Map size" -> replyMarkup.setReplyMarkup(bottons.getSizeButtons());
                case "Scale" -> replyMarkup.setReplyMarkup(bottons.getScaleButtons());
                case "Height difference" -> replyMarkup.setReplyMarkup(bottons.getHeightDifferenceButtons());
                case "Islands modifier" -> replyMarkup.setReplyMarkup(bottons.getIslandsModifierButtons());
                case "Get last map" -> {
                    Optional<UserParameters> userPatamLastMapOptional = userParametersRepository.findByChatId(chatId);
                    replyMarkup.setReplyMarkup(bottons.getLastMapButton(userPatamLastMapOptional));
                }
                case "Generate" -> {
                    var up = userParametersRepository.findByChatId(chatId).orElse(null);
                    sendPhoto = botContent.createSendPhotoTerraNotaMapImage(chatId, up);
                    TerraNotaMap t = terraNotaMapReporitory.save(botContent.getTerraNotaMap());
                    setUsernameDateMapId(up, update, t);
                    userParametersRepository.save(up);
                    replyMarkup.setReplyMarkup(bottons.getLastMapButton(Optional.of(up)));
                }
                case "Small" -> {
                    userParameters.setMapSize(129);
                    updateProcess(userParametersRepository, replyMarkup, userParameters);
                }
                case "Medium" -> {
                    userParameters.setMapSize(513);
                    updateProcess(userParametersRepository, replyMarkup, userParameters);
                }
                case "Large" -> {
                    userParameters.setMapSize(713);
                    updateProcess(userParametersRepository, replyMarkup, userParameters);
                }
                case "x1" -> {
                    userParameters.setScale(1);
                    updateProcess(userParametersRepository, replyMarkup, userParameters);
                }
                case "x2" -> {
                    userParameters.setScale(2);
                    updateProcess(userParametersRepository, replyMarkup, userParameters);
                }
                case "x4" -> {
                    userParameters.setScale(4);
                    updateProcess(userParametersRepository, replyMarkup, userParameters);
                }
                case "Smooth" -> {
                    userParameters.setHeightDifference(2);
                    updateProcess(userParametersRepository, replyMarkup, userParameters);
                }
                case "Hill" -> {
                    userParameters.setHeightDifference(4);
                    updateProcess(userParametersRepository, replyMarkup, userParameters);
                }
                case "Mountain" -> {
                    userParameters.setHeightDifference(9);
                    updateProcess(userParametersRepository, replyMarkup, userParameters);
                }
                case "Islands" -> {
                    userParameters.setIslandsModifier(1);
                    updateProcess(userParametersRepository, replyMarkup, userParameters);
                }
                case "Backwater" -> {
                    userParameters.setIslandsModifier(10);
                    updateProcess(userParametersRepository, replyMarkup, userParameters);
                }
                case "Continent" -> {
                    userParameters.setIslandsModifier(40);
                    updateProcess(userParametersRepository, replyMarkup, userParameters);
                }
                case "back" -> replyMarkup.setReplyMarkup(bottons.getMainButtons(userParameters));

                default -> {
                    if (callbackData.contains("get map ")) {
                        callbackData = callbackData.substring("get map ".length()).trim();
                        var usersParamByMapId = userParametersRepository.findByMapid(callbackData);
                        Optional<TerraNotaMap> terraNotaMap = Optional.empty();
                        if (usersParamByMapId.isPresent())
                            terraNotaMap = terraNotaMapReporitory.findById(usersParamByMapId.get().getMapid());
                        sendPhoto = botContent.getExistedPhoto(chatId, terraNotaMap.orElse(null), usersParamByMapId.orElse(null));
                        Optional<UserParameters> userPatamLastMapOptional = userParametersRepository.findByChatIdAndMaxDate(chatId);
                        replyMarkup.setReplyMarkup(bottons.getLastMapButton(userPatamLastMapOptional));
                    }

                    else if(callbackData.contains("Water level +1 ")){
                        callbackData = callbackData.substring("Water level +1 ".length()).trim();
                        setReplyMarkupUpdateData(callbackData, replyMarkup, -1, "Water level +1 ", sendPhoto, chatId);
                    }
                    else if(callbackData.contains("Water level -1 ")){
                        callbackData = callbackData.substring("Water level -1 ".length()).trim();
                        setReplyMarkupUpdateData(callbackData, replyMarkup, 1, "Water level -1 ", sendPhoto, chatId);
                    }
                    else if(callbackData.contains("Water level +5 ")){
                        callbackData = callbackData.substring("Water level +5 ".length()).trim();
                        setReplyMarkupUpdateData(callbackData, replyMarkup, -5, "Water level +5 ", sendPhoto, chatId);
                    }
                    else if(callbackData.contains("Water level -5 ")){
                        callbackData = callbackData.substring("Water level -5 ".length()).trim();
                        setReplyMarkupUpdateData(callbackData, replyMarkup, +5, "Water level -5 ", sendPhoto, chatId);
                    }
                    else if (callbackData.contains("back to map ")) {
                        replyMarkup.setReplyMarkup(bottons.getMainButtons(userParameters));
                    }
                    else if(callbackData.contains("control water level ")){
                        callbackData = callbackData.substring("control water level ".length()).trim();
                        replyMarkup.setReplyMarkup(bottons.getControlWaterLevelBottons(callbackData));
                    }
                    else {
                        replyMarkup.setReplyMarkup(bottons.getMapManipulationButtons(callbackData));
                    }
                }
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

    private void setReplyMarkupUpdateData(String callbackData, EditMessageReplyMarkup replyMarkup,
                                          int value, String type, SendPhoto sendPhoto, long chatId) {
        var usersParamByMapId = userParametersRepository.findByMapid(callbackData);
        Optional<TerraNotaMap> terraNotaMap = Optional.empty();
        if (usersParamByMapId.isPresent())
            terraNotaMap = terraNotaMapReporitory.findById(usersParamByMapId.get().getMapid());
        if(!Objects.equals(type, "get map ")) {
            TerraNotaMap changedMapHeights = botContent.changeMapHeights(terraNotaMap.orElse(null), value);
            terraNotaMapReporitory.save(changedMapHeights);
            replyMarkup.setReplyMarkup(bottons.getLastMapButton(usersParamByMapId));
        } else {
            sendPhoto = botContent.getExistedPhoto(chatId, terraNotaMap.orElse(null), usersParamByMapId.orElse(null));
            Optional<UserParameters> userPatamLastMapOptional = userParametersRepository.findByChatIdAndMaxDate(chatId);
            replyMarkup.setReplyMarkup(bottons.getLastMapButton(userPatamLastMapOptional));
        }
    }

    private void updateProcess(UserParametersRepository userParametersRepository,
                               EditMessageReplyMarkup replyMarkup,
                               UserParameters userParameters) {
        userParametersRepository.save(userParameters);
        replyMarkup.setReplyMarkup(bottons.getMainButtons(userParameters));
    }

    private void setUsernameDateMapId(UserParameters up, Update update, TerraNotaMap t) {
        up.setUsername(update.getCallbackQuery().getFrom().getUserName());
        up.setLocalDateTime(LocalDateTime.now());
        up.setMapid(t.getId());
    }

    private EditMessageReplyMarkup getReplyMarkup(long chatId, int messageId) {
        EditMessageReplyMarkup replyMarkup = new EditMessageReplyMarkup();
        replyMarkup.setChatId(chatId);
        replyMarkup.setMessageId(messageId);
        return replyMarkup;
    }

    private UserParameters getUserParameters(long chatId, Update update, int messageId) {
        var i = userParametersRepository.findByChatId(chatId);
        if (i.isPresent()) {
            log.info("Getting existed UserParameters");
            return i.get();
        } else {
            log.info("New UserParameters created");
            UserParameters userParameters = UserParameters.getDefaultWithUpdate(update, messageId, chatId);
            userParametersRepository.save(userParameters);
            return userParameters;
        }
    }

    private void createSendMessage(Update update){
        SendMessage sendMessage = botContent.createSendMessage(update);
        sendMessage.setReplyMarkup(bottons.getMainButtons(null));
        try {
            execute(sendMessage);
        } catch (Exception e) {
            log.error("In first try " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return telegramBotConfig.getBotName();
    }
}
