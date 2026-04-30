package org.example.Achievement;

public enum AchievementCategory {
    COLLECTION("Collection"),
    GAME_TIME("Survival"),
    LENGTH("Length"),
    SPECIAL("Special");

    public final String displayName;

    AchievementCategory(String displayName) {
        this.displayName = displayName;
    }
}
