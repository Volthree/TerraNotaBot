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
    BACK("back");

    private final String value;

    MainButtons(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }
}
