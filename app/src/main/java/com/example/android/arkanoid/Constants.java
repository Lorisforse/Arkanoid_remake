package com.example.android.arkanoid;

public class Constants {

    private static boolean flag = false;
    private static int gameMode = 4;
    private Constants() {}


    public static boolean getFlag() {
        return flag;
    }
    public static void setFlag(boolean flag) {
        Constants.flag = flag;
    }
    public static int getGameMode() {
        return gameMode;
    }
    public static void setGameMode(int gameMode) {
        Constants.gameMode = gameMode;
    }
}
