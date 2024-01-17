package vladislavmaltsev.terranotabot.service.enums;

public enum MainButtons {
    SIZE("sizeButton"),
    SCALE("scaleButton"),
    HEIGHT_DIFFERENCE("heightDifferenceButton"),
    ISLANDS_MODIFIER("heightDifferenceButton"),
    GET_LAST_MAP("getLastGenerationButton"),
    GET_PREVIOUS_MAP("getPreviousGeneratedButton"),
    GENERATE("generateButton"),
    SMALL("SMALL"),
    MEDIUM("MEDIUM"),
    LARGE("LARGE"),
    BACK("back"),

    X_1("x1"),
    X_2("x2"),
    X_4("x4"),

    SMOOTH("Smooth"),
    HILL("Hill"),
    MOUNTAIN("Mountain"),

    ISLANDS("Islands"),
    BLACKWATER("Blackwater"),
    CONTINENT("Continent");

    private final String value;

    MainButtons(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }
}
