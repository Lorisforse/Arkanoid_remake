package com.example.android.arkanoid;

public class Constants {

    private static boolean flag = false;

    private Constants() {}


    public static boolean getFlag() {
        return flag;
    }
    public static void setFlag(boolean flag) {
        Constants.flag = flag;
    }
}
