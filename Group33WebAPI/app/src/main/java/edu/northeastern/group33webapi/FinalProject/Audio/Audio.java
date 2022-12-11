package edu.northeastern.group33webapi.FinalProject.Audio;

import android.content.Context;
import android.media.MediaPlayer;

import edu.northeastern.group33webapi.FinalProject.Constants;
import edu.northeastern.group33webapi.R;

public class Audio {
    public MediaPlayer bgm;
    public MediaPlayer obstacleAudio;
    public MediaPlayer coinAudio;
    public MediaPlayer addHpAudio;
    public MediaPlayer gameOverAudio;
    public boolean isSoundOn;

    public Audio(Context context) {
        this.bgm = MediaPlayer.create(context, R.raw.bgm);;
        this.obstacleAudio = MediaPlayer.create(context, R.raw.obstacle);;
        this.coinAudio = MediaPlayer.create(context, R.raw.coin);
        this.addHpAudio = MediaPlayer.create(context, R.raw.addhp);
        this.gameOverAudio = MediaPlayer.create(context, R.raw.gameover);
        this.isSoundOn = true;
    }
}
