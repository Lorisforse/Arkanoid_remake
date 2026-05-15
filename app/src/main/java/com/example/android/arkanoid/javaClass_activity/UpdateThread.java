package com.example.android.arkanoid.javaClass_activity;

import android.os.Handler;

public class UpdateThread extends Thread {
    Handler updatovaciHandler;
    private volatile boolean running = true;

    public UpdateThread(Handler uh) {
        super();
        updatovaciHandler = uh;
    }

    public void stopThread() {
        running = false;
    }

    public void run() {
        while (running) {
            try {
                sleep(32);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            }
            updatovaciHandler.sendEmptyMessage(0);
        }
    }
}
