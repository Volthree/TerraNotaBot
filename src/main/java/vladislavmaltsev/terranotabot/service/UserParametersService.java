package vladislavmaltsev.terranotabot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import vladislavmaltsev.terranotabot.enity.UserParameters;
import vladislavmaltsev.terranotabot.repository.UserParametersRepository;

@Service
@Slf4j
public class UserParametersService {

    private final UserParametersRepository userParametersRepository;

    public UserParametersService(UserParametersRepository userParametersRepository) {
        this.userParametersRepository = userParametersRepository;
    }

    public UserParameters getUserParametersDependsExisted(long chatId, Update update, int messageId) {
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
}
