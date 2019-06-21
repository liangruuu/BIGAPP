package cn.edu.zucc.bigapp.music;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;

import cn.edu.zucc.bigapp.R;
import cn.edu.zucc.bigapp.music.model.Music;
import cn.edu.zucc.bigapp.music.model.staticData;


public class MusicActivity extends AppCompatActivity {
  private TextView musicTime, musicTotal, musicName, musicArtist, commonTvTitle;
  private ImageView commonIvBack;
  private SeekBar seekBar;
  private int id = 0;
  private int pattern = 1;
  private Music music;
  private ObjectAnimator animator;
  private Button btnPlayOrPause, btnPrev, btnNext, btnPattern;
  private SimpleDateFormat time = new SimpleDateFormat("mm:ss",Locale.CHINA);
  private String[] patternname = {"列表", "随机", "单曲"};
  private boolean tag1 = false;
  private boolean tag2 = false;
  private MusicService musicService;


  private void bindServiceConnection() {
    Intent intent = new Intent(MusicActivity.this, MusicService.class);
    startService(intent);
    bindService(intent, serviceConnection, MusicActivity.BIND_AUTO_CREATE);
  }

  private ServiceConnection serviceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      musicService = ((MusicService.MyBinder) (service)).getService();
      musicService.mediaPlayer.setOnCompletionListener(map->btnNext.performClick());
      musicService.tag=false;
      musicService.stop();
      btnPlayOrPause.performClick();

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }


  };

  public Handler handler = new Handler();
  public Runnable runnable = new Runnable() {
    @Override
    public void run() {
      musicTime.setText(time.format(musicService.mediaPlayer.getCurrentPosition()));
      seekBar.setProgress(musicService.mediaPlayer.getCurrentPosition());
      seekBar.setMax(musicService.mediaPlayer.getDuration());
      musicTotal.setText(time.format(musicService.mediaPlayer.getDuration()));
      handler.postDelayed(runnable, 200);
    }
  };

  private void findViewById() {
    musicTime =  findViewById(R.id.MusicTime);
    musicTotal =  findViewById(R.id.MusicTotal);
    musicName =  findViewById(R.id.musicname);
    musicArtist =  findViewById(R.id.musicartist);
    seekBar =  findViewById(R.id.MusicSeekBar);
    btnPlayOrPause =  findViewById(R.id.BtnPlayorPause);
    btnPrev =  findViewById(R.id.BtnPrev);
    btnNext =  findViewById(R.id.BtnNext);
    btnPattern = findViewById(R.id.BtnPattern);
    commonIvBack = findViewById(R.id.common_iv_back);
    commonTvTitle = findViewById(R.id.common_tv_title);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {


    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_music);
    findViewById();
    init();
    myListener();
    bindServiceConnection();
    commonIvBack.setOnClickListener(v -> {
      StoreActivity.startActivity(MusicActivity.this);

    }
    );
    commonTvTitle.setText("音乐播放");
    ActionBar actionBar = getSupportActionBar();
      if (actionBar != null) {
          actionBar.hide();
      }
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  public void init() {
    btnPlayOrPause.setText("播放");
    id = staticData.index;
    pattern = staticData.pattern;
    btnPattern.setText(patternname[pattern - 1]);
    tag1 = false;
    tag2 = false;
    music = staticData.musicList.get(id);
    musicName.setText(music.getTitle());
    musicArtist.setText(music.getArtist());

    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser == true) {
          musicService.mediaPlayer.seekTo(progress);
        }

      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });
  }

  private void myListener() {
    ImageView imageView = (ImageView) findViewById(R.id.Image);
    animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360.0f);
    animator.setDuration(10000);
    animator.setInterpolator(new LinearInterpolator());
    animator.setRepeatCount(-1);
    btnPlayOrPause.setOnClickListener(new View.OnClickListener() {
      @TargetApi(Build.VERSION_CODES.KITKAT)
      @Override
      public void onClick(View v) {
        if (musicService.mediaPlayer != null) {
          seekBar.setProgress(musicService.mediaPlayer.getCurrentPosition());
          seekBar.setMax(musicService.mediaPlayer.getDuration());
        }
        //  由tag的变换来控制事件的调用
        if (musicService.tag != true) {
          btnPlayOrPause.setText("暂停");

          musicService.playOrPause();
          musicService.tag = true;

          if (tag1 == false) {
            animator.start();
            tag1 = true;
          } else {
            animator.resume();
          }
        } else {
          btnPlayOrPause.setText("播放");

          musicService.playOrPause();
          animator.pause();
          musicService.tag = false;
        }
        if (tag2 == false) {
          handler.post(runnable);
          tag2 = true;
        }
      }
    });
    //上一首
    btnPrev.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (pattern == 1) {
          id -= 1;
          if (id < 0)
            id = staticData.musicList.size() - 1;
          staticData.index = id;
          tag1 = false;
          tag2 = false;
          musicService.tag = false;
          music = staticData.musicList.get(id);
          musicService.stop();
          musicName.setText(music.getTitle());
          musicArtist.setText(music.getArtist());
          btnPlayOrPause.performClick();

        } else if (pattern == 2) {
          while (id == staticData.index) {
            id = new Random().nextInt(staticData.musicList.size());
          }
          staticData.index = id;
          tag1 = false;
          tag2 = false;
          musicService.tag = false;
          music = staticData.musicList.get(id);
          musicService.stop();
          musicName.setText(music.getTitle());
          musicArtist.setText(music.getArtist());
          btnPlayOrPause.performClick();

        } else {
          tag1 = false;
          tag2 = false;
          musicService.tag = false;
          musicService.stop();
          musicName.setText(music.getTitle());
          musicArtist.setText(music.getArtist());
          btnPlayOrPause.performClick();
        }
      }
    });

    //下一首
    btnNext.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (pattern == 1) {
          id += 1;
          if (id == staticData.musicList.size())
            id = 0;
          staticData.index = id;
          tag1 = false;
          tag2 = false;
          musicService.tag = false;
          music = staticData.musicList.get(id);
          musicService.stop();
          musicName.setText(music.getTitle());
          musicArtist.setText(music.getArtist());
          btnPlayOrPause.performClick();

        } else if (pattern == 2) {
          while (id == staticData.index) {
            id = new Random().nextInt(staticData.musicList.size());
          }
          staticData.index = id;
          tag1 = false;
          tag2 = false;
          musicService.tag = false;
          music = staticData.musicList.get(id);
          musicService.stop();
          musicName.setText(music.getTitle());
          musicArtist.setText(music.getArtist());
          btnPlayOrPause.performClick();

        } else {
          tag1 = false;
          tag2 = false;
          musicService.tag = false;
          musicService.stop();
          musicName.setText(music.getTitle());
          musicArtist.setText(music.getArtist());
          btnPlayOrPause.performClick();
        }
      }
    });
    btnPattern.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        pattern += 1;
        if (pattern == 4)
          pattern = 1;
        staticData.pattern = pattern;
        btnPattern.setText(patternname[pattern - 1]);
      }
    });

  }

  //  获取并设置返回键的点击事件
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
        StoreActivity.startActivity(MusicActivity.this);
        onStop();
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

}



