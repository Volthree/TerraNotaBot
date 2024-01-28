package vladislavmaltsev.terranotabot.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import vladislavmaltsev.terranotabot.annotations.LogAnn;
import vladislavmaltsev.terranotabot.config.TelegramBotConfig;
import vladislavmaltsev.terranotabot.dto.UserParametersDTO;
import vladislavmaltsev.terranotabot.enity.UserParameters;
import vladislavmaltsev.terranotabot.mapgeneration.map.TerraNotaMap;
import vladislavmaltsev.terranotabot.repository.UserParametersRepository;

import java.util.Optional;

import static vladislavmaltsev.terranotabot.util.JsonData.JsonParser.fromJson;

@Service
@Slf4j
public class TerraNotaBotLongPolling extends TelegramLongPollingBot {
    private final TelegramBotConfig telegramBotConfig;
    private final BottonsService bottonsService;
    private final BotContentService botContentService;
    private final UserParametersRepository userParametersRepository;
    private final UserParametersService userParametersService;
    private final UpdateService updateService;
    private final ReplyMarkupService replyMarkupService;
    private final PhotoService photoService;
    private final SendMessageService sendMessageService;

    @Autowired
    public TerraNotaBotLongPolling(TelegramBotConfig telegramBotConfig,
                                   BottonsService bottonsService, BotContentService botContentService,
                                   UserParametersRepository userParametersRepository,
                                   UserParametersService userParametersService,
                                   UpdateService updateService,
                                   ReplyMarkupService replyMarkupService,
                                   PhotoService photoService, SendMessageService sendMessageService) {
        super(telegramBotConfig.getToken());
        this.telegramBotConfig = telegramBotConfig;
        this.bottonsService = bottonsService;
        this.botContentService = botContentService;
        this.userParametersRepository = userParametersRepository;
        this.userParametersService = userParametersService;
        this.updateService = updateService;
        this.replyMarkupService = replyMarkupService;
        this.photoService = photoService;
        this.sendMessageService = sendMessageService;
    }

    @SneakyThrows
    @Override
    @LogAnn
    @Transactional
    public void onUpdateReceived(Update update) {
        SendPhoto sendPhoto = null;
        SendMessage sendMessage = null;
        UserParametersDTO userParameters = null;
        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (update.getMessage().getText()) {
                case "/start", "/menu" -> {
                    sendMessage = sendMessageService.getDefaultMessage(update, userParameters);
                    execute(sendMessage);
                }
            }
        } else if (update.hasCallbackQuery()) {
            updateService.setUpdate(update);
            int messageId = updateService.getMessageId();
            long chatId = updateService.getChatId();
            String callbackData = updateService.getCallbackQuery();
            userParameters = userParametersService
                    .getUserParametersDependsExisted(chatId, update, messageId);
            replyMarkupService.setReplyMarkupId(chatId, messageId);
            switch (callbackData) {
                case "Map size" -> replyMarkupService.setReplyMarkupKeyboard(bottonsService.getSizeButtons());
                case "Scale" -> replyMarkupService.setReplyMarkupKeyboard(bottonsService.getScaleButtons());
                case "Height difference" ->
                        replyMarkupService.setReplyMarkupKeyboard(bottonsService.getHeightDifferenceButtons());
                case "Islands modifier" ->
                        replyMarkupService.setReplyMarkupKeyboard(bottonsService.getIslandsModifierButtons());
                case "Get last map" ->
                        replyMarkupService.setReplyMarkupKeyboard(bottonsService.getLastMapButton(userParametersRepository.findByChatId(chatId)));
                case "Small" -> userParametersService.mapSizeParameter(userParameters, 129, replyMarkupService);
                case "Medium" -> userParametersService.mapSizeParameter(userParameters, 513, replyMarkupService);
                case "Large" -> userParametersService.mapSizeParameter(userParameters, 713, replyMarkupService);
                case "x1" -> userParametersService.mapScaleParameter(userParameters, 1, replyMarkupService);
                case "x2" -> userParametersService.mapScaleParameter(userParameters, 2, replyMarkupService);
                case "x4" -> userParametersService.mapScaleParameter(userParameters, 4, replyMarkupService);
                case "Smooth" -> userParametersService.mapHeightParameter(userParameters, 2, replyMarkupService);
                case "Hill" -> userParametersService.mapHeightParameter(userParameters, 4, replyMarkupService);
                case "Mountain" -> userParametersService.mapHeightParameter(userParameters, 9, replyMarkupService);
                case "Islands" -> userParametersService.mapIslandParameter(userParameters, 1, replyMarkupService);
                case "Backwater" -> userParametersService.mapIslandParameter(userParameters, 10, replyMarkupService);
                case "Continent" -> userParametersService.mapIslandParameter(userParameters, 40, replyMarkupService);
                case "back" -> replyMarkupService.setReplyMarkupKeyboard(bottonsService.getMainButtons(userParameters));
                case "Generate" -> {
                    var up = userParametersRepository.findByChatId(chatId).orElseThrow();
                    sendPhoto = botContentService.getSendPhoto(chatId, null, up);
                    TerraNotaMap t = botContentService.getTerraNotaMap();
                    updateService.setUsernameDateMapIdMapHash(up, update, t);
                    userParametersRepository.save(up);
                    replyMarkupService.setReplyMarkupKeyboard(bottonsService.getLastMapButton(Optional.of(up)));
                }
                default -> {
                    if (callbackData.contains("get map ")) {
                        callbackData = callbackData.substring("get map ".length()).trim();
                        var usersParamByMapHash = userParametersRepository.findByMapHash(Integer.parseInt(callbackData));
                        Optional<TerraNotaMap> terraNotaMap = Optional.empty();
                        if (usersParamByMapHash.isPresent())
                            terraNotaMap = Optional.of(fromJson(usersParamByMapHash.get().getMapid()));
                        sendPhoto = botContentService.getSendPhoto(chatId, terraNotaMap.orElse(null), null);
                        Optional<UserParameters> userPatamLastMapOptional = userParametersRepository.findByChatId(chatId);
                        replyMarkupService.setReplyMarkupKeyboard(bottonsService.getLastMapButton(userPatamLastMapOptional));
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
                        replyMarkupService.setReplyMarkupKeyboard(bottonsService.getMainButtons(userParameters));
                    } else if (callbackData.contains("control water level ")) {
                        callbackData = callbackData.substring("control water level ".length()).trim();
                        replyMarkupService.setReplyMarkupKeyboard(bottonsService.getControlWaterLevelBottons(callbackData));
                    } else {
                        replyMarkupService.setReplyMarkupKeyboard(bottonsService.getMapManipulationButtons(callbackData));
                    }
                }
            }
        }
        try {
            if (sendPhoto != null)
                execute(photoService.getSendPhoto());
            if (replyMarkupService.getReplyMarkup() != null)
                execute(replyMarkupService.getReplyMarkup());
//            if (sendMessage != null)
//                execute(sendMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return telegramBotConfig.getBotName();
    }
}
