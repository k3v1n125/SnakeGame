package org.game;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class SavePaths {

    private static final String APP_NAME = "SnakeGame";

    private SavePaths() {
    }

    public static String appSupportFile(String fileName) {
        return appSupportDirectory().resolve(fileName).toString();
    }

    public static File appSupportDirectoryFile() {
        return appSupportDirectory().toFile();
    }

    private static Path appSupportDirectory() {
        String userHome = System.getProperty("user.home");
        return Paths.get(userHome, "Library", "Application Support", APP_NAME);
    }
}