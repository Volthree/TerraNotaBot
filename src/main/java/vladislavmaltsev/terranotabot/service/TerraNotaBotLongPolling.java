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
import vladislavmaltsev.terranotabot.repository.UserParametersRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import static vladislavmaltsev.terranotabot.util.JsonData.JsonParser.*;

@Service
@Slf4j
public class TerraNotaBotLongPolling extends TelegramLongPollingBot {
    private final TelegramBotConfig telegramBotConfig;
    private final BottonsService bottonsService;
    private final BotContentService botContentService;
    private final UserParametersRepository userParametersRepository;
    private final MapService mapService;
    private final UserParametersService userParametersService;
    private final UpdateService updateService;
    private final ReplyMarkupService replyMarkupService;

    @Autowired
    public TerraNotaBotLongPolling(TelegramBotConfig telegramBotConfig,
                                   BottonsService bottonsService, BotContentService botContentService,
                                   UserParametersRepository userParametersRepository, MapService mapService, UserParametersService userParametersService, UpdateService updateService, ReplyMarkupService replyMarkupService) {
        super(telegramBotConfig.getToken());
        this.telegramBotConfig = telegramBotConfig;
        this.bottonsService = bottonsService;
        this.botContentService = botContentService;
        this.userParametersRepository = userParametersRepository;
        this.mapService = mapService;
        this.userParametersService = userParametersService;
        this.updateService = updateService;
        this.replyMarkupService = replyMarkupService;
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
            updateService.setUpdate(update);
            int messageId = updateService.getMessageId();
            long chatId = updateService.getChatId();
            String callbackData = updateService.getCallbackQuery();
            userParameters = userParametersService.getUserParametersDependsExisted(chatId, update, messageId);
            replyMarkupService.setReplyMarkup(chatId, messageId);

            switch (callbackData) {
                case "Map size" -> replyMarkupService.setReplyMarkup(bottonsService.getSizeButtons());
                case "Scale" -> replyMarkupService.setReplyMarkup(bottonsService.getScaleButtons());
                case "Height difference" -> replyMarkupService.setReplyMarkup(bottonsService.getHeightDifferenceButtons());
                case "Islands modifier" -> replyMarkupService.setReplyMarkup(bottonsService.getIslandsModifierButtons());
                case "Get last map" -> {
                    Optional<UserParameters> userPatamLastMapOptional = userParametersRepository.findByChatId(chatId);
                    replyMarkupService.setReplyMarkup(bottonsService.getLastMapButton(userPatamLastMapOptional));
                }
                case "Generate" -> {
                    var up = userParametersRepository.findByChatId(chatId).orElse(null);
                    sendPhoto = botContentService.getSendPhoto(chatId, null, up);
                    TerraNotaMap t = botContentService.getTerraNotaMap();
                    setUsernameDateAndMapIdAndMapHash(up, update, t);
                    userParametersRepository.save(up);
                    replyMarkupService.setReplyMarkup(bottonsService.getLastMapButton(Optional.of(up)));
                }
                case "Small" -> {
                    userParameters.setMapSize(129);
                    updateProcess(userParametersRepository, replyMarkupService.getReplyMarkup(), userParameters);
                }
                case "Medium" -> {
                    userParameters.setMapSize(513);
                    updateProcess(userParametersRepository, replyMarkupService.getReplyMarkup(), userParameters);
                }
                case "Large" -> {
                    userParameters.setMapSize(713);
                    updateProcess(userParametersRepository, replyMarkupService.getReplyMarkup(), userParameters);
                }
                case "x1" -> {
                    userParameters.setScale(1);
                    updateProcess(userParametersRepository, replyMarkupService.getReplyMarkup(), userParameters);
                }
                case "x2" -> {
                    userParameters.setScale(2);
                    updateProcess(userParametersRepository, replyMarkupService.getReplyMarkup(), userParameters);
                }
                case "x4" -> {
                    userParameters.setScale(4);
                    updateProcess(userParametersRepository, replyMarkupService.getReplyMarkup(), userParameters);
                }
                case "Smooth" -> {
                    userParameters.setHeightDifference(2);
                    updateProcess(userParametersRepository, replyMarkupService.getReplyMarkup(), userParameters);
                }
                case "Hill" -> {
                    userParameters.setHeightDifference(4);
                    updateProcess(userParametersRepository, replyMarkupService.getReplyMarkup(), userParameters);
                }
                case "Mountain" -> {
                    userParameters.setHeightDifference(9);
                    updateProcess(userParametersRepository, replyMarkupService.getReplyMarkup(), userParameters);
                }
                case "Islands" -> {
                    userParameters.setIslandsModifier(1);
                    updateProcess(userParametersRepository, replyMarkupService.getReplyMarkup(), userParameters);
                }
                case "Backwater" -> {
                    userParameters.setIslandsModifier(10);
                    updateProcess(userParametersRepository, replyMarkupService.getReplyMarkup(), userParameters);
                }
                case "Continent" -> {
                    userParameters.setIslandsModifier(40);
                    updateProcess(userParametersRepository, replyMarkupService.getReplyMarkup(), userParameters);
                }
                case "back" -> replyMarkupService.setReplyMarkup(bottonsService.getMainButtons(userParameters));

                default -> {
                    if (callbackData.contains("get map ")) {
                        callbackData = callbackData.substring("get map ".length()).trim();
                        var usersParamByMapHash = userParametersRepository.findByMapHash(Integer.parseInt(callbackData));
                        Optional<TerraNotaMap> terraNotaMap = Optional.empty();
                        if (usersParamByMapHash.isPresent())
                            terraNotaMap = Optional.of(fromJson(usersParamByMapHash.get().getMapid()));
                        sendPhoto = botContentService.getSendPhoto(chatId, terraNotaMap.orElse(null), null);
                        Optional<UserParameters> userPatamLastMapOptional = userParametersRepository.findByChatId(chatId);
                        replyMarkupService.setReplyMarkup(bottonsService.getLastMapButton(userPatamLastMapOptional));
                    } else if (callbackData.contains("Water level +1 ")) {
                        callbackData = callbackData.substring("Water level +1 ".length()).trim();
                        replyMarkupService.setReplyMarkupUpdateData(callbackData, replyMarkupService.getReplyMarkup(), -1, "Water level +1 ", sendPhoto, chatId);
                    } else if (callbackData.contains("Water level -1 ")) {
                        callbackData = callbackData.substring("Water level -1 ".length()).trim();
                        replyMarkupService.setReplyMarkupUpdateData(callbackData, replyMarkupService.getReplyMarkup(), 1, "Water level -1 ", sendPhoto, chatId);
                    } else if (callbackData.contains("Water level +5 ")) {
                        callbackData = callbackData.substring("Water level +5 ".length()).trim();
                        replyMarkupService.setReplyMarkupUpdateData(callbackData, replyMarkupService.getReplyMarkup(), -5, "Water level +5 ", sendPhoto, chatId);
                    } else if (callbackData.contains("Water level -5 ")) {
                        callbackData = callbackData.substring("Water level -5 ".length()).trim();
                        replyMarkupService.setReplyMarkupUpdateData(callbackData, replyMarkupService.getReplyMarkup(), +5, "Water level -5 ", sendPhoto, chatId);
                    } else if (callbackData.contains("back to map ")) {
                        replyMarkupService.setReplyMarkup(bottonsService.getMainButtons(userParameters));
                    } else if (callbackData.contains("control water level ")) {
                        callbackData = callbackData.substring("control water level ".length()).trim();
                        replyMarkupService.setReplyMarkup(bottonsService.getControlWaterLevelBottons(callbackData));
                    } else {
                        replyMarkupService.setReplyMarkup(bottonsService.getMapManipulationButtons(callbackData));
                    }
                }
            }
            try {
                if (sendPhoto != null) {
                    execute(sendPhoto);
                    log.warn("Photo sand");
                }
                execute(replyMarkupService.getReplyMarkup());
                log.warn("Reply markup sand");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
    private void updateProcess(UserParametersRepository userParametersRepository,
                               EditMessageReplyMarkup replyMarkup,
                               UserParameters userParameters) {
        userParametersRepository.save(userParameters);
        replyMarkup.setReplyMarkup(bottonsService.getMainButtons(userParameters));
    }

    private void setUsernameDateAndMapIdAndMapHash(UserParameters up, Update update, TerraNotaMap t) {
        up.setUsername(update.getCallbackQuery().getFrom().getUserName());
        up.setLocalDateTime(LocalDateTime.now());
        up.setMapHash(t.hashCode());
        up.setMapid(toJson(t));
    }

    private void createSendMessage(Update update) {
        SendMessage sendMessage = botContentService.createSendMessage(update);
        sendMessage.setReplyMarkup(bottonsService.getMainButtons(null));
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
