package cn.edu.zucc.bigapp.music;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import cn.edu.zucc.bigapp.music.model.staticData;

public class MusicService extends Service {
  public MediaPlayer mediaPlayer;
  public boolean tag = false;

  public MusicService() {
    mediaPlayer = new MediaPlayer();
    stop();
  }
  //  通过 Binder 来保持 Activity 和 Service 的通信
  public MyBinder binder = new MyBinder();

  public class MyBinder extends Binder {
    MusicService getService() {
      return MusicService.this;
    }
  }



  public void playOrPause() {
    if (mediaPlayer.isPlaying()) {
      mediaPlayer.pause();
    } else {
      mediaPlayer.start();
    }
  }

  public void stop() {
    if (mediaPlayer != null) {
      mediaPlayer.stop();
      try {
        mediaPlayer.reset();
        mediaPlayer.setDataSource(staticData.musicList.get(staticData.index).getPath());
        mediaPlayer.prepare();

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public boolean onUnbind(Intent intent) {
    return true;
  }

  @Override
  public IBinder onBind(Intent intent) {
    stop();
    return binder;
  }

}

