package vladislavmaltsev.terranotabot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import vladislavmaltsev.terranotabot.enity.UserParameters;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class Bottons {


    public InlineKeyboardButton createButton(String setText, String buttonName) {
        var i = new InlineKeyboardButton();
        i.setText(setText);
        i.setCallbackData(buttonName);
        return i;
    }

    public InlineKeyboardButton createButtonFromString(String setText, String button) {
        var i = new InlineKeyboardButton();
        i.setText(setText);
        i.setCallbackData(button);
        return i;
    }

    public InlineKeyboardMarkup getSizeButtons() {

        var row1Button = List.of(
                createButton("Small", "Small"),
                createButton("Medium", "Medium"),
                createButton("Large", "Large")
        );
        var row2Button = List.of(
                createButton("back", "back")
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
                createButton("Smooth", "Smooth"),
                createButton("Hill", "Hill"),
                createButton("Mountain", "Mountain")
        );
        var row2Button = List.of(
                createButton("back", "back")
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
                createButton("Islands", "Islands"),
                createButton("Backwater", "Backwater"),
                createButton("Continent", "Continent")
        );
        var row2Button = List.of(
                createButton("back", "back")
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
                createButton("x1", "x1"),
                createButton("x2", "x2"),
                createButton("x4", "x4")
        );
        var row2Button = List.of(
                createButton("back", "back")
        );
        var rowsButton = List.of(
                row1Button,
                row2Button
        );
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowsButton);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getLastMapButton(Optional<UserParameters> userParameters) {
        List<List<InlineKeyboardButton>> rowsButton = new ArrayList<>();
        if (userParameters.isPresent() && userParameters.get().getMapid() != null) {
            var row1Button = List.of(
                    createButtonFromString(
                            userParameters.get().getLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE)
                                    + " : edit or get existed map",
//                            userParameters.get().getMapid()
                            String.valueOf(userParameters.get().getMapHash())
                            )
            );
            rowsButton.add(row1Button);
        }

        var row2Button = List.of(
                createButton("back", "back")
        );
        rowsButton.add(row2Button);

        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowsButton);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getMapManipulationButtons(String callBackData) {

        var row1Button = List.of(
                createButton("control water level", "control water level " + callBackData)
        );
        var row2Button = List.of(
                createButton("get map", "get map " + callBackData)

        );
        var row3Button = List.of(
                createButton("back", "back")
        );
        var rowsButton = List.of(
                row1Button,
                row2Button,
                row3Button
        );
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowsButton);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getControlWaterLevelBottons(String callBackData) {

        var row1Button = List.of(
                createButton("Water level +1", "Water level +1 " + callBackData),
                createButton("Water level -1", "Water level -1 "  + callBackData)
        );
        var row2Button = List.of(
                createButton("Water level +5", "Water level +5 "  + callBackData),
                createButton("Water level -5", "Water level -5 "  + callBackData)

        );
        var row3Button = List.of(
                createButton("back to map", "back to manipulation map")
        );
        var rowsButton = List.of(
                row1Button,
                row2Button,
                row3Button
        );
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowsButton);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getPreviousMapsButton(List<UserParameters> userParametersList) {
        List<List<InlineKeyboardButton>> rowsButton = new ArrayList<>();
        for (UserParameters u : userParametersList) {
            rowsButton.add(
                    List.of(
                            createButtonFromString(
                                    u.getLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE)
                                            + " h: "
                                            + u.hashCode(),
                                    u.getMapid()
                            )
                    )
            );
        }
        var row2Button = List.of(
                createButton("back", "back")
        );
        rowsButton.add(row2Button);

        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowsButton);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getMainButtons(UserParameters userParameters) {

        var row1Button = List.of(
                createButton("Map size "+ (userParameters !=null ? userParameters.getMapSizeInString() : ""), "Map size"),
                createButton("Scale "+ (userParameters !=null ? userParameters.getScaleInString() : ""), "Scale")
        );
        var row2Button = List.of(
                createButton("Height difference "+(userParameters !=null ? userParameters.getHeightDifferenceInString() : ""), "Height difference"),
                createButton("Islands modifier "+(userParameters !=null ? userParameters.getIslandModifierInString() : ""), "Islands modifier")

        );
        var row3Button = List.of(
                createButton("Get last map ", "Get last map")
//                , createButton("Get previous map", "Get previous map")

        );
        var row4Button = List.of(
                createButton("Generate", "Generate")

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
