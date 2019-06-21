package cn.edu.zucc.bigapp.run.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.zucc.bigapp.R;
import cn.edu.zucc.bigapp.run.bean.RunBean;
import cn.edu.zucc.bigapp.run.db.RunDatabaseHelper;
import cn.edu.zucc.bigapp.run.utils.COSserver;

public class RunHistoryActivity extends AppCompatActivity {

  @BindView(R.id.common_iv_back)
  ImageView commonIvBack;
  @BindView(R.id.common_tv_title)
  TextView commonTvTitle;
  @BindView(R.id.common_title_ll)
  LinearLayout commonTitleLl;
  @BindView(R.id.main_rv_show_run)
  RecyclerView mMainRvShowRun;
  @BindView(R.id.backup)
  ImageView backup;
  @BindView(R.id.backdown)
  ImageView backdown;


  private List<RunBean> mRunBeanList;
  private RunDatabaseHelper mHelper;

  public static void startActivity(Context context) {
    Intent intent = new Intent(context, RunHistoryActivity.class);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_run_history);
    ButterKnife.bind(this);

    mHelper = new RunDatabaseHelper(this, "Run.db", null, 3);

    ActionBar actionBar = getSupportActionBar();
    actionBar.hide();
    getRunBeanList();
    if (getRunBeanList().size() == 0) {
      Log.d("jianglr", "yes");
    } else {
      Log.d("jianglr", "no");
    }
    for (RunBean rb : getRunBeanList()) {
      Log.d("jianglr", rb.getTime() + ":" + rb.getDistance() + ":" + rb.getTime());
    }
    mMainRvShowRun.setLayoutManager(new LinearLayoutManager(this));
    mMainRvShowRun.setAdapter(new RunAdpter(this, mRunBeanList));
  }

  private List<RunBean> getRunBeanList() {
    mRunBeanList = new ArrayList<>();
    List<RunBean> runList = new ArrayList<>();
    SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();
    Cursor cursor = sqLiteDatabase.query("Run", null, null, null, null, null, null);
    if (cursor.moveToFirst()) {
      do {
        String date = cursor.getString(cursor.getColumnIndex("date"));
        String distance = cursor.getString(cursor.getColumnIndex("distance"));
        String time = cursor.getString(cursor.getColumnIndex("time"));
//        Log.d("xxx", distance + "-" + date + "-" + time);
        runList.add(new RunBean(distance, date, time));
      } while (cursor.moveToNext());
    }
    cursor.close();
    mRunBeanList = runList;
    return mRunBeanList;
  }


  @OnClick({R.id.common_iv_back, R.id.backup, R.id.backdown})
  public void onClick(View view) {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    switch (view.getId()) {
      case R.id.common_iv_back:
        StartRunActivity.startActivity(this);
        break;
      case R.id.backup:
        alertDialogBuilder.setMessage("你确定要备份吗？").setPositiveButton("确定", (dialogInterface, i) -> {
          COSserver yun = new COSserver(getApplicationContext());
          yun.upload("/data/data/cn.edu.zucc.bigapp/databases/Run.db", "Run.db");
        }).setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss()).show();
        break;
      case R.id.backdown:
        alertDialogBuilder.setMessage("你确定要恢复吗？").setPositiveButton("确定", (dialogInterface, i) -> {
          COSserver yun = new COSserver(getApplicationContext());
          yun.download("Run.db");
        }).setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss()).show();
        break;
    }
  }
}
