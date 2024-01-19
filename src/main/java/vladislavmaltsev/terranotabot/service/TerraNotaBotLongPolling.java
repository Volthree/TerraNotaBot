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
import vladislavmaltsev.terranotabot.enity.MapHeights;
import vladislavmaltsev.terranotabot.enity.UserParameters;
import vladislavmaltsev.terranotabot.mapgeneration.map.TerraNotaMap;
import vladislavmaltsev.terranotabot.repository.MapHeightsRepository;
import vladislavmaltsev.terranotabot.repository.UserParametersRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TerraNotaBotLongPolling extends TelegramLongPollingBot {
    private final TelegramBotConfig telegramBotConfig;
    private final Bottons bottons;
    private final BotContent botContent;
    private final UserParametersRepository userParametersRepository;

    private final MapHeightsRepository mapHeightsRepository;
    private final List<UserParameters> userParametersList = new ArrayList<>();

    @Autowired
    public TerraNotaBotLongPolling(TelegramBotConfig telegramBotConfig,
                                   Bottons bottons, BotContent botContent,
                                   UserParametersRepository userParametersRepository,
                                   MapHeightsRepository mapHeightsRepository) {
        super(telegramBotConfig.getToken());
        this.telegramBotConfig = telegramBotConfig;
        this.bottons = bottons;
        this.botContent = botContent;
        this.userParametersRepository = userParametersRepository;
        this.mapHeightsRepository = mapHeightsRepository;
    }

    @Override
    @LogAnn
    @Transactional
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = null;
        SendPhoto sendPhoto = null;
        UserParameters userParameters;
        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (update.getMessage().getText()) {
                case "/start", "/menu" -> {
                    sendMessage = botContent.createSendMessage(update);
                    sendMessage.setReplyMarkup(bottons.getMainButtons());
                }
            }
            try {
                if (sendMessage != null)
                    execute(sendMessage);
            } catch (Exception e) {
                log.error("In first try 64 " + e.getMessage());
            }
        } else if (update.hasCallbackQuery()) {
            int messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            EditMessageReplyMarkup replyMarkup = new EditMessageReplyMarkup();
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
            log.error("CallBackData " + callbackData);
//            MainButtonsEnum mainButton = MainButtonsEnum.valueOf(callbackData);
            replyMarkup.setChatId(chatId);
            replyMarkup.setMessageId(messageId);
            switch (callbackData) {
                case "Map size" -> replyMarkup.setReplyMarkup(bottons.getSizeButtons());
                case "Scale" -> replyMarkup.setReplyMarkup(bottons.getScaleButtons());
                case "Height difference" -> replyMarkup.setReplyMarkup(bottons.getHeightDifferenceButtons());
                case "Islands modifier" -> replyMarkup.setReplyMarkup(bottons.getIslandsModifierButtons());
                case "Get last map" -> {
                    Optional<UserParameters> userPatamLastMapOptional = userParametersRepository.findByChatIdAndMaxDate(chatId);
                    replyMarkup.setReplyMarkup(bottons.getLastMapButton(userPatamLastMapOptional));
                }
                case "Get previous map" -> {
                    List<UserParameters> userPatamListPreviousMaps = userParametersRepository.findByChatId(chatId);
                    replyMarkup.setReplyMarkup(bottons.getPreviousMapsButton(userPatamListPreviousMaps));
//                    replyMarkup.setReplyMarkup(bottons.getSizeButtons());
                    System.out.println("AFTER REPLY MARKUP");
                }
                case "Generate" -> {
                    sendPhoto = botContent.createSendPhoto(chatId,
                            userParameters.getMapSize(),
                            userParameters.getMapSize(),
                            userParameters.getScale(),
                            userParameters.getHeightDifference(),
                            userParameters.getIslandsModifier());
                    TerraNotaMap terraNotaMap = botContent.getTerraNotaMap();
                    MapHeights s = mapHeightsRepository.save(terraNotaMap.getMapHeights());

                    var userParametersToSave = UserParameters.builder()
                            .updateId(update.getUpdateId())
                            .messageId(messageId)
                            .chatId(chatId)
                            .mapSize(userParameters.getMapSize())
                            .scale(userParameters.getScale())
                            .heightDifference(userParameters.getHeightDifference())
                            .islandsModifier(userParameters.getIslandsModifier())
                            .username(update.getCallbackQuery().getFrom().getUserName())
                            .localDateTime(LocalDateTime.now())
                            .mapid(s.getId())
                            .build();

                    replyMarkup.setReplyMarkup(bottons.getSizeButtons());
                    userParametersRepository.save(userParametersToSave);
                }
                case "Small" -> {
                    userParameters.setMapSize(129);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case "Medium" -> {
                    userParameters.setMapSize(513);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case "Large" -> {
                    userParameters.setMapSize(713);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case "x1" -> {
                    userParameters.setScale(1);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case "x2" -> {
                    userParameters.setScale(2);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case "x4" -> {
                    userParameters.setScale(4);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case "Smooth" -> {
                    userParameters.setHeightDifference(2);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case "Hill" -> {
                    userParameters.setHeightDifference(4);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case "Mountain" -> {
                    userParameters.setHeightDifference(9);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case "Islands" -> {
                    userParameters.setIslandsModifier(1);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case "Backwater" -> {
                    userParameters.setIslandsModifier(10);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case "Continent" -> {
                    userParameters.setIslandsModifier(40);
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case "back" -> {
                    log.info(userParameters.toString());
                    replyMarkup.setReplyMarkup(bottons.getMainButtons());
                }
                case "back to map" -> {

                }
                default -> {
                    if (callbackData.contains("get map ")) {
                        callbackData = callbackData.substring("get map ".length()).trim();
                        var usersParamByMapId = userParametersRepository.findByMapid(callbackData);
                        Optional<MapHeights> map = Optional.empty();
                        if (usersParamByMapId.isPresent())
                            map = mapHeightsRepository.findById(usersParamByMapId.get().getMapid());
                        sendPhoto = botContent.getExistedPhoto(chatId, map.orElse(null), usersParamByMapId.orElse(null));
                        Optional<UserParameters> userPatamLastMapOptional = userParametersRepository.findByChatIdAndMaxDate(chatId);
                        replyMarkup.setReplyMarkup(bottons.getLastMapButton(userPatamLastMapOptional));
                    }
                    else if (callbackData.contains("back to map ")) {
                        replyMarkup.setReplyMarkup(bottons.getMainButtons());
                    }
                    else if(callbackData.contains("control water level ")){
                        callbackData = callbackData.substring("control water level ".length()).trim();
                        replyMarkup.setReplyMarkup(bottons.getControlWaterLevelBottons(callbackData));
                    }
                    else if(callbackData.contains("Water level +1 ")){
                        callbackData = callbackData.substring("Water level +1 ".length()).trim();
                        var usersParamByMapId = userParametersRepository.findByMapid(callbackData);
                        Optional<MapHeights> map = Optional.empty();
                        if (usersParamByMapId.isPresent())
                            map = mapHeightsRepository.findById(usersParamByMapId.get().getMapid());
                        MapHeights changedMapHeights = botContent.changeMapHeights(map.orElse(null), -1);
                        mapHeightsRepository.save(changedMapHeights);
                        replyMarkup.setReplyMarkup(bottons.getLastMapButton(usersParamByMapId));
                    }
                    else if(callbackData.contains("Water level -1 ")){
                        callbackData = callbackData.substring("Water level -1 ".length()).trim();
                        var usersParamByMapId = userParametersRepository.findByMapid(callbackData);
                        Optional<MapHeights> map = Optional.empty();
                        if (usersParamByMapId.isPresent())
                            map = mapHeightsRepository.findById(usersParamByMapId.get().getMapid());
                        MapHeights changedMapHeights = botContent.changeMapHeights(map.orElse(null), 1);
                        mapHeightsRepository.save(changedMapHeights);
                        replyMarkup.setReplyMarkup(bottons.getLastMapButton(usersParamByMapId));
                    }
                    else if(callbackData.contains("Water level +5 ")){
                        callbackData = callbackData.substring("Water level +5 ".length()).trim();
                        var usersParamByMapId = userParametersRepository.findByMapid(callbackData);
                        Optional<MapHeights> map = Optional.empty();
                        if (usersParamByMapId.isPresent())
                            map = mapHeightsRepository.findById(usersParamByMapId.get().getMapid());
                        MapHeights changedMapHeights = botContent.changeMapHeights(map.orElse(null), -5);
                        mapHeightsRepository.save(changedMapHeights);
                        replyMarkup.setReplyMarkup(bottons.getLastMapButton(usersParamByMapId));
                    }
                    else if(callbackData.contains("Water level -5 ")){
                        callbackData = callbackData.substring("Water level -5 ".length()).trim();
                        var usersParamByMapId = userParametersRepository.findByMapid(callbackData);
                        Optional<MapHeights> map = Optional.empty();
                        if (usersParamByMapId.isPresent())
                            map = mapHeightsRepository.findById(usersParamByMapId.get().getMapid());
                        MapHeights changedMapHeights = botContent.changeMapHeights(map.orElse(null), 5);
                        mapHeightsRepository.save(changedMapHeights);
                        replyMarkup.setReplyMarkup(bottons.getLastMapButton(usersParamByMapId));
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


    @Override
    public String getBotUsername() {
        return telegramBotConfig.getBotName();
    }
}
