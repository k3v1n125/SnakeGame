package org.example.Achievement;

public enum Achievement {
    FIRST_APPLE ("First Bite", "Collect your first apple"),
    TEN_APPLES ("Apple Hoarder", "Collect 10 apples in one game"),
    TWENTY_APPLES ("Glutton", "Collect 20 apples in one game"),
    LENGTH_TEN ("Growing Up", "Reach a snake length of 10"),
    LENGTH_TWENTY ("Long Boy", "Reach a snake length of 20"),
    SURVIVE_30 ("Survivor", "Survive for 30 seconds"),
    SURVIVE_60 ("Veteran", "Survive for 60 seconds"),
    SURVIVE_120 ("Legend", "Survive for 120 seconds"),
    SPEED_DEMON ("Speed Demon", "Collect an apple in under 2 seconds"),
    PERFECTIONIST ("Perfectionist", "Collect 5 apples without missing one");

    public final String title;
    public final String description;

    Achievement(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
