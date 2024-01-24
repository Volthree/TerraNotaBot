package vladislavmaltsev.terranotabot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    public TerraNotaBotLongPolling(TelegramBotConfig telegramBotConfig,
                                   Bottons bottons, BotContent botContent,
                                   UserParametersRepository userParametersRepository) {
        super(telegramBotConfig.getToken());
        this.telegramBotConfig = telegramBotConfig;
        this.bottons = bottons;
        this.botContent = botContent;
        this.userParametersRepository = userParametersRepository;
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

            userParameters = getUserParametersDependsExisted(chatId, update, messageId);
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
                    // OLD TerraNotaMap t = terraNotaMapReporitory.save(botContent.getTerraNotaMap());
                    // new
                    TerraNotaMap t = botContent.getTerraNotaMap();
                    // end new
                    setUsernameDateAndMapIdAndMapHash(up, update, t);
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
                        var usersParamByMapHash = userParametersRepository.findByMapHash(Integer.valueOf(callbackData));
                        Optional<TerraNotaMap> terraNotaMap = Optional.empty();
                        if (usersParamByMapHash.isPresent())
                            //OLD terraNotaMap = terraNotaMapReporitory.findById(usersParamByMapId.get().getMapid());
                            //new
                            terraNotaMap = Optional.of(fromJson(usersParamByMapHash.get().getMapid()));
                        //end new
                        sendPhoto = botContent.getExistedPhoto(chatId, terraNotaMap.orElse(null), usersParamByMapHash.orElse(null));
                        Optional<UserParameters> userPatamLastMapOptional = userParametersRepository.findByChatId(chatId);
                        replyMarkup.setReplyMarkup(bottons.getLastMapButton(userPatamLastMapOptional));
                    } else if (callbackData.contains("Water level +1 ")) {
                        callbackData = callbackData.substring("Water level +1 ".length()).trim();
                        setReplyMarkupUpdateData(callbackData, replyMarkup, -1, "Water level +1 ", sendPhoto, chatId);
                    } else if (callbackData.contains("Water level -1 ")) {
                        callbackData = callbackData.substring("Water level -1 ".length()).trim();
                        setReplyMarkupUpdateData(callbackData, replyMarkup, 1, "Water level -1 ", sendPhoto, chatId);
                    } else if (callbackData.contains("Water level +5 ")) {
                        callbackData = callbackData.substring("Water level +5 ".length()).trim();
                        setReplyMarkupUpdateData(callbackData, replyMarkup, -5, "Water level +5 ", sendPhoto, chatId);
                    } else if (callbackData.contains("Water level -5 ")) {
                        callbackData = callbackData.substring("Water level -5 ".length()).trim();
                        setReplyMarkupUpdateData(callbackData, replyMarkup, +5, "Water level -5 ", sendPhoto, chatId);
                    } else if (callbackData.contains("back to map ")) {
                        replyMarkup.setReplyMarkup(bottons.getMainButtons(userParameters));
                    } else if (callbackData.contains("control water level ")) {
                        callbackData = callbackData.substring("control water level ".length()).trim();
                        replyMarkup.setReplyMarkup(bottons.getControlWaterLevelBottons(callbackData));
                    } else {
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
        var usersParamByMapId = userParametersRepository.findByMapHash(Integer.valueOf(callbackData));
        Optional<TerraNotaMap> terraNotaMap = Optional.empty();
        if (usersParamByMapId.isPresent())
            //OLD terraNotaMap = terraNotaMapReporitory.findById(usersParamByMapId.get().getMapid());
            //new
            terraNotaMap = Optional.of(fromJson(usersParamByMapId.get().getMapid()));
        //end new
        if (!Objects.equals(type, "get map ")) {
            TerraNotaMap changedMapHeights = botContent.changeMapHeights(terraNotaMap.orElse(null), value);
            usersParamByMapId.get().setMapid(toJson(terraNotaMap.get()));
            usersParamByMapId = Optional.of(userParametersRepository.save(usersParamByMapId.get()));
//            terraNotaMapReporitory.save(changedMapHeights);

            replyMarkup.setReplyMarkup(bottons.getLastMapButton(usersParamByMapId));
        } else {
            sendPhoto = botContent.getExistedPhoto(chatId, terraNotaMap.orElse(null), usersParamByMapId.orElse(null));
            Optional<UserParameters> userPatamLastMapOptional = userParametersRepository.findByChatId(chatId);
            replyMarkup.setReplyMarkup(bottons.getLastMapButton(userPatamLastMapOptional));
        }
    }

    private void updateProcess(UserParametersRepository userParametersRepository,
                               EditMessageReplyMarkup replyMarkup,
                               UserParameters userParameters) {
        userParametersRepository.save(userParameters);
        replyMarkup.setReplyMarkup(bottons.getMainButtons(userParameters));
    }

    private void setUsernameDateAndMapIdAndMapHash(UserParameters up, Update update, TerraNotaMap t) {
        up.setUsername(update.getCallbackQuery().getFrom().getUserName());
        up.setLocalDateTime(LocalDateTime.now());
        up.setMapHash(t.hashCode());
        up.setMapid(toJson(t));
    }

    private EditMessageReplyMarkup getReplyMarkup(long chatId, int messageId) {
        EditMessageReplyMarkup replyMarkup = new EditMessageReplyMarkup();
        replyMarkup.setChatId(chatId);
        replyMarkup.setMessageId(messageId);
        return replyMarkup;
    }

    private UserParameters getUserParametersDependsExisted(long chatId, Update update, int messageId) {
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

    private void createSendMessage(Update update) {
        SendMessage sendMessage = botContent.createSendMessage(update);
        sendMessage.setReplyMarkup(bottons.getMainButtons(null));
        try {
            execute(sendMessage);
        } catch (Exception e) {
            log.error("In first try " + e.getMessage());
        }
    }

    public static String toJson(TerraNotaMap terraNotaMap) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(terraNotaMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static TerraNotaMap fromJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, TerraNotaMap.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return telegramBotConfig.getBotName();
    }
}
