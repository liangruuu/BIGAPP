package cn.edu.zucc.bigapp.music;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.edu.zucc.bigapp.R;
import cn.edu.zucc.bigapp.diary.ui.DiaryActivity;
import cn.edu.zucc.bigapp.music.model.Music;
import cn.edu.zucc.bigapp.music.model.staticData;

public class StoreActivity extends AppCompatActivity {
  private ImageView commonIvBack;
  private TextView commonTvTitle;

  public static void startActivity(Context context) {
    Intent intent = new Intent(context, StoreActivity.class);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_store);
    staticData.musicList = getMusicInfo();

    findViewById();

    ActionBar actionBar = getSupportActionBar();
    actionBar.hide();

    commonIvBack.setOnClickListener(view ->
        DiaryActivity.startActivity(this));
    commonTvTitle.setText("音乐");

    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view1);
    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    MyAdapter myAdapter = new MyAdapter(staticData.musicList, getApplicationContext());
    recyclerView.setAdapter(myAdapter);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
  }

  @TargetApi(Build.VERSION_CODES.M)
  private void checkpermission() {
    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {

      // Should we show an explanation?
      if (shouldShowRequestPermissionRationale(
          Manifest.permission.READ_EXTERNAL_STORAGE)) {
        // Explain to the user why we need to read the contacts
      }

      requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
          R.layout.activity_store);

      // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
      // app-defined int constant that should be quite unique

      return;
    }
  }

  private void findViewById() {
    commonIvBack = findViewById(R.id.common_iv_back);
    commonTvTitle = findViewById(R.id.common_tv_title);
  }

  private List<Music> getMusicInfo() {
    //TODO 创建一个集合存储读取的歌曲信息
    List<Music> musicList = new ArrayList<Music>();
    SimpleDateFormat format = new SimpleDateFormat("mm:ss");
    checkpermission();
    //TODO 读取数据库中歌曲信息
    Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    for (int i = 0; i < cursor.getCount(); i++) {
      Music music = new Music();
      cursor.moveToNext();

      // 歌曲
      music.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
      // 歌手
      music.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
      // 时长
      music.setDuration(format.format(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))));
      // 文件大小
      music.setSize(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)) * 1.0 / 1024);
      // 路径
      music.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));

      // 将资源为音乐的媒体文件存储到集合中
      if (cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)) != 0) {
        musicList.add(music);
      }
    }

    cursor.close();

    return musicList;
  }

}
