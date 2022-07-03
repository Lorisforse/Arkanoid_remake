package com.example.android.arkanoid;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class SoundPlayer {

    private static  SoundPool soundPool;

  private static int s_brick;
    // private static int s_paddle;
    // private static int s_menu;
   // MediaPlayer s_brick;
    MediaPlayer s_paddle;
    MediaPlayer s_menu;
    MediaPlayer s_gameOver;
    MediaPlayer s_win;
    MediaPlayer s_ice;
    MediaPlayer s_fire;
    MediaPlayer s_life;


    public SoundPlayer(Context context) {

        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 10);

        s_brick = soundPool.load(context,R.raw.arkanoid,1);
        // s_paddle = soundPool.load(context, R.raw.arkpaddle, 1);
        // s_menu = soundPool.load(context, R.raw.menusound, 1);
        // s_brick = MediaPlayer.create(context, R.raw.arkanoid);
        s_paddle = MediaPlayer.create(context,R.raw.arkpaddle);
        s_menu = MediaPlayer.create(context,R.raw.menusound);
        s_gameOver = MediaPlayer.create(context,R.raw.gameoversound);
        s_win = MediaPlayer.create(context,R.raw.winsound);
        s_ice = MediaPlayer.create(context,R.raw.iceballsound);
        s_fire = MediaPlayer.create(context,R.raw.fireballsound);
        s_life = MediaPlayer.create(context,R.raw.lifesound);



    }

    public void playLife(){
        s_life.start();
    }

    public void playFire(){
        s_fire.start();
    }

    public void playIce(){
        s_ice.start();
    }

    public void playWin(){
        s_win.start();
    }

    public void playGameOver(){
        s_gameOver.start();
    }
    public void playBrick(){

      soundPool.play(s_brick, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void stopSoundBrick(){
        //s_brick.stop();
        soundPool.stop(s_brick);
    }

    public void playPaddle(){
      s_paddle.start();

    }

    public void playMenu() {
        if (Constants.musicActive) {
            s_menu.start();
            s_menu.setVolume(0.3f, 0.3f);
            s_menu.setLooping(true);
        }
    }
}
