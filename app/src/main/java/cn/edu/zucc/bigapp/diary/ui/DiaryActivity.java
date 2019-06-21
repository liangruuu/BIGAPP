package cn.edu.zucc.bigapp.diary.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.zucc.bigapp.R;
import cn.edu.zucc.bigapp.diary.bean.DiaryBean;
import cn.edu.zucc.bigapp.diary.db.DiaryDatabaseHelper;
import cn.edu.zucc.bigapp.diary.event.StartUpdateDiaryEvent;
import cn.edu.zucc.bigapp.diary.utils.AppManager;
import cn.edu.zucc.bigapp.diary.utils.GetDate;
import cn.edu.zucc.bigapp.diary.utils.StatusBarCompat;
import cn.edu.zucc.bigapp.music.StoreActivity;
import cn.edu.zucc.bigapp.news.ui.NewsActivity;
import cn.edu.zucc.bigapp.run.ui.StartRunActivity;
import cn.edu.zucc.bigapp.run.utils.COSserver;

public class DiaryActivity extends AppCompatActivity {


  @BindView(R.id.main_dr_main)
  DrawerLayout mDrawerLayout;
  @BindView(R.id.common_iv_back)
  ImageView mCommonIvBack;
  @BindView(R.id.common_tv_title)
  TextView mCommonTvTitle;
  @BindView(R.id.common_title_ll)
  LinearLayout mCommonTitleLl;
  @BindView(R.id.main_iv_circle)
  ImageView mMainIvCircle;
  @BindView(R.id.main_tv_date)
  TextView mMainTvDate;
  @BindView(R.id.main_tv_content)
  TextView mMainTvContent;
  @BindView(R.id.item_ll_control)
  LinearLayout mItemLlControl;

  @BindView(R.id.main_rv_show_diary)
  RecyclerView mMainRvShowDiary;
  @BindView(R.id.main_fab_enter_edit)
  FloatingActionButton mMainFabEnterEdit;
  @BindView(R.id.main_rl_main)
  RelativeLayout mMainRlMain;
  @BindView(R.id.item_first)
  LinearLayout mItemFirst;
  @BindView(R.id.main_ll_main)
  LinearLayout mMainLlMain;
  @BindView(R.id.nav_view)
  NavigationView mNavView;
  @BindView(R.id.backup)
  ImageView backup;
  @BindView(R.id.backdown)
  ImageView backdown;
  @BindView(R.id.srl_diary)
  SwipeRefreshLayout srlDiary;
  private List<DiaryBean> mDiaryBeanList;

  private DiaryDatabaseHelper mHelper;
  private Context mContext;

  public static void startActivity(Context context) {
    Intent intent = new Intent(context, DiaryActivity.class);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_diary);
    AppManager.getAppManager().addActivity(this);
    ButterKnife.bind(this);
    mContext = this;
    StatusBarCompat.compat(this, Color.parseColor("#161414"));
    mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);
    ActionBar actionBar = getSupportActionBar();
    actionBar.hide();
    EventBus.getDefault().register(this);
    getDiaryBeanList();
    initTitle();
    mMainRvShowDiary.setLayoutManager(new LinearLayoutManager(this));
    mMainRvShowDiary.setAdapter(new DiaryAdapter(this, mDiaryBeanList));

    srlDiary.setOnRefreshListener(() -> {
      mDiaryBeanList = getDiaryBeanList();
      srlDiary.setRefreshing(false);
      mMainRvShowDiary.setAdapter(new DiaryAdapter(mContext, mDiaryBeanList));
    });


    mNavView.setCheckedItem(R.id.nav_diary);
    mNavView.setNavigationItemSelectedListener(menuItem -> {
      switch (menuItem.getItemId()) {
        case R.id.nav_news:
          NewsActivity.startActivity(this);
          break;
        case R.id.nav_run:
          StartRunActivity.startActivity(this);
          break;
        case R.id.nav_music:
          StoreActivity.startActivity(this);
          break;
      }
      return true;
    });
  }

  private void initTitle() {
    mMainTvDate.setText("今天，" + GetDate.getDate());
    mCommonTvTitle.setText("日记");
    //    mCommonIvBack.setVisibility(View.INVISIBLE);
    mCommonIvBack.setImageResource(R.drawable.list);
  }

  private List<DiaryBean> getDiaryBeanList() {

    mDiaryBeanList = new ArrayList<>();
    List<DiaryBean> diaryList = new ArrayList<>();
    SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();
    Cursor cursor = sqLiteDatabase.query("Diary", null, null, null, null, null, null);

    if (cursor.moveToFirst()) {
      do {
        String date = cursor.getString(cursor.getColumnIndex("date"));
        String dateSystem = GetDate.getDate().toString();
        if (date.equals(dateSystem)) {
          mMainLlMain.removeView(mItemFirst);
          break;
        }
      } while (cursor.moveToNext());
    }

    if (cursor.moveToFirst()) {
      do {
        String date = cursor.getString(cursor.getColumnIndex("date"));
        String title = cursor.getString(cursor.getColumnIndex("title"));
        String content = cursor.getString(cursor.getColumnIndex("content"));
        String tag = cursor.getString(cursor.getColumnIndex("tag"));
        mDiaryBeanList.add(new DiaryBean(date, title, content, tag));
      } while (cursor.moveToNext());
    }
    cursor.close();

    for (int i = mDiaryBeanList.size() - 1; i >= 0; i--) {
      diaryList.add(mDiaryBeanList.get(i));
    }

    mDiaryBeanList = diaryList;
    return mDiaryBeanList;
  }

  @Subscribe
  public void startUpdateDiaryActivity(StartUpdateDiaryEvent event) {
    String title = mDiaryBeanList.get(event.getPosition()).getTitle();
    String content = mDiaryBeanList.get(event.getPosition()).getContent();
    String tag = mDiaryBeanList.get(event.getPosition()).getTag();
    UpdateDiaryActivity.startActivity(this, title, content, tag);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  @OnClick({R.id.main_fab_enter_edit, R.id.common_iv_back, R.id.backup, R.id.backdown})
  public void onClick(View view) {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    switch (view.getId()) {
      case R.id.main_fab_enter_edit:
        AddDiaryActivity.startActivity(this);
        break;
      case R.id.common_iv_back:
        mDrawerLayout.openDrawer(GravityCompat.START);
        break;
      case R.id.backup:
        alertDialogBuilder.setMessage("你确定要备份吗？").setPositiveButton("确定", (dialogInterface, i) -> {
          COSserver yun = new COSserver(getApplicationContext());
          yun.upload("/data/data/cn.edu.zucc.bigapp/databases/Diary.db", "Diary.db");
        }).setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss()).show();
        break;
      case R.id.backdown:
        alertDialogBuilder.setMessage("你确定要恢复吗？").setPositiveButton("确定", (dialogInterface, i) -> {
          COSserver yun = new COSserver(getApplicationContext());
          yun.download("Diary.db");
        }).setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss()).show();
        break;
    }
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    AppManager.getAppManager().AppExit(this);
  }
}
