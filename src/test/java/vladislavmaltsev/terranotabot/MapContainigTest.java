package vladislavmaltsev.terranotabot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import vladislavmaltsev.terranotabot.components.maincomponents.HeightDiffComponent;
import vladislavmaltsev.terranotabot.components.maincomponents.IslandsComponent;
import vladislavmaltsev.terranotabot.components.maincomponents.MapSizeComponent;
import vladislavmaltsev.terranotabot.components.maincomponents.ScaleComponent;
import vladislavmaltsev.terranotabot.components.interfaces.ReplyMarcupInt;
import vladislavmaltsev.terranotabot.repository.UserParametersRepository;
import vladislavmaltsev.terranotabot.service.BottonsService;
import vladislavmaltsev.terranotabot.service.ReplyMarkupService;
import vladislavmaltsev.terranotabot.service.UpdateService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest(classes = {
        HeightDiffComponent.class,
        IslandsComponent.class,
        MapSizeComponent.class,
        ScaleComponent.class
})
public class MapContainigTest {
    @Autowired
    List<ReplyMarcupInt> replyMarcupIntList;
    Map<String, ReplyMarcupInt> replyMarcupInterfaceMap;
    @MockBean
    ReplyMarkupService replyMarkupService;
    @MockBean
    BottonsService bottonsService;
    @MockBean
    UserParametersRepository userParametersRepository;
    @MockBean
    UpdateService updateService;

    @Test
    void mapContent() {
        replyMarcupInterfaceMap = replyMarcupIntList.stream().
                collect(Collectors.toMap(
                        ReplyMarcupInt::getId, x -> x
                ));
        Assertions.assertThat(replyMarcupInterfaceMap).isNotEmpty();

        ReplyMarcupInt replyMarcupInt = replyMarcupInterfaceMap.get("Map size");
        Assertions.assertThat(replyMarcupInt)
                .isNotNull()
                .isExactlyInstanceOf(MapSizeComponent.class);
    }
}
