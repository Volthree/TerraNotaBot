package vladislavmaltsev.terranotabot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import vladislavmaltsev.terranotabot.service.enums.MainButtonsEnum;

import java.util.List;

import static vladislavmaltsev.terranotabot.service.enums.MainButtonsEnum.*;
import static vladislavmaltsev.terranotabot.service.enums.MainButtonsEnum.BACK;

@Service
public class Bottons {


    public InlineKeyboardButton createButton(String setText, MainButtonsEnum button) {
        var i = new InlineKeyboardButton();
        i.setText(setText);
        i.setCallbackData(button.toString());
        return i;
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
}
