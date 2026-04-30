package org.example.Achievement;

public enum Achievement {
    FIRST_APPLE ("First Bite", "Collect your first apple", AchievementCategory.COLLECTION),
    TEN_APPLES ("Apple Hoarder", "Collect 10 apples in one game", AchievementCategory.COLLECTION),
    TWENTY_APPLES ("Glutton", "Collect 20 apples in one game", AchievementCategory.COLLECTION),
    SPEED_DEMON ("Speed Demon", "Collect an apple in under 2 seconds", AchievementCategory.COLLECTION),
    PERFECTIONIST ("Perfectionist", "Collect 5 apples without missing one", AchievementCategory.COLLECTION),
    STAR_KEEPER ("Star Keeper", "Collect 5 stars without letting any disappear", AchievementCategory.COLLECTION),
    SURVIVE_30 ("Survivor", "Survive for 30 seconds", AchievementCategory.GAME_TIME),
    SURVIVE_60 ("Veteran", "Survive for 60 seconds", AchievementCategory.GAME_TIME),
    SURVIVE_120 ("Legend", "Survive for 120 seconds", AchievementCategory.GAME_TIME),
    LENGTH_TEN ("Growing Up", "Reach a snake length of 10", AchievementCategory.LENGTH),
    LENGTH_TWENTY ("Long Boy", "Reach a snake length of 20", AchievementCategory.LENGTH),
    SURVIVE_60_NO_APPLE ("Apple Hater", "Survive for 60 seconds without collecting an apple until game ends", AchievementCategory.SPECIAL),
    SURVIVE_120_NO_APPLE ("Stay Short", "Survive for 120 seconds without collecting an apple until game ends", AchievementCategory.SPECIAL),
    BOARD_FILLER ("Board Filler", "Fill the entire board with the snake", AchievementCategory.SPECIAL);

    public final String title;
    public final String description;
    public final AchievementCategory category;

    Achievement(String title, String description, AchievementCategory category) {
        this.title = title;
        this.description = description;
        this.category = category;
    }
}
