package com.example.android.arkanoid.javaClass_activity;

public class Constants {


    public static SoundPlayer sound;

    public static boolean musicActive = true;

    private static int gameMode = 4;

    private static int soundPosition = 0;

    private Constants() {}

    public static int getGameMode() {
        return gameMode;
    }
    public static void setGameMode(int gameMode) {
        Constants.gameMode = gameMode;
    }
    public static int getSoundPosition() {
        return soundPosition;
    }
    public static void setSoundPosition(int soundPosition) {
        Constants.soundPosition = soundPosition;
    }
}
