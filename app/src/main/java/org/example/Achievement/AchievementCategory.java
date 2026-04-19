package org.example.Achievement;

public enum AchievementCategory {
    COLLECT_APPLE("Collect Apple"),
    COLLECT_STAR("Collect Star"),
    GAME_TIME("Game Time"),
    LENGTH("Length"),
    SPECIAL("Special");

    public final String displayName;

    AchievementCategory(String displayName) {
        this.displayName = displayName;
    }
}
