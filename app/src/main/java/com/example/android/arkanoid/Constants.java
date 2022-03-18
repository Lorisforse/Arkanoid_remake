package com.example.android.arkanoid;

public class Constants {

    private static boolean flag = false;

    public static SoundPlayer sound;

    private static int gameMode = 4;

    private static int soundPosition = 0;

    private Constants() {}

    public static int getGameMode() {
        return gameMode;
    }
    public static void setGameMode(int gameMode) {
        Constants.gameMode = gameMode;
    }
    public static boolean getFlag() {
        return flag;
    }
    public static void setFlag(boolean flag) {
        Constants.flag = flag;
    }
    public static int getSoundPosition() {
        return soundPosition;
    }
    public static void setSoundPosition(int soundPosition) {
        Constants.soundPosition = soundPosition;
    }
}
