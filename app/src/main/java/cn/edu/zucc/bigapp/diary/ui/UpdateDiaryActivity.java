package cn.edu.zucc.bigapp.diary.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.zucc.bigapp.R;
import cn.edu.zucc.bigapp.diary.db.DiaryDatabaseHelper;
import cn.edu.zucc.bigapp.diary.utils.AppManager;
import cn.edu.zucc.bigapp.diary.utils.GetDate;
import cn.edu.zucc.bigapp.diary.utils.StatusBarCompat;
import cn.edu.zucc.bigapp.diary.widget.LinedEditText;


public class UpdateDiaryActivity extends AppCompatActivity {

  @BindView(R.id.update_diary_tv_date)
  TextView mUpdateDiaryTvDate;
  @BindView(R.id.update_diary_et_title)
  EditText mUpdateDiaryEtTitle;
  @BindView(R.id.update_diary_et_content)
  LinedEditText mUpdateDiaryEtContent;
  @BindView(R.id.update_diary_fab_back)
  FloatingActionButton mUpdateDiaryFabBack;
  @BindView(R.id.update_diary_fab_add)
  FloatingActionButton mUpdateDiaryFabAdd;
  @BindView(R.id.update_diary_fab_delete)
  FloatingActionButton mUpdateDiaryFabDelete;
  @BindView(R.id.right_labels)
  FloatingActionsMenu mRightLabels;
  @BindView(R.id.common_tv_title)
  TextView mCommonTvTitle;
  @BindView(R.id.common_title_ll)
  LinearLayout mCommonTitleLl;
  @BindView(R.id.common_iv_back)
  ImageView mCommonIvBack;
  @BindView(R.id.update_diary_tv_tag)
  TextView mTvTag;

  private DiaryDatabaseHelper mHelper;

  public static void startActivity(Context context, String title, String content, String tag) {
    Intent intent = new Intent(context, UpdateDiaryActivity.class);
    intent.putExtra("title", title);
    intent.putExtra("content", content);
    intent.putExtra("tag", tag);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_update_diary);
    AppManager.getAppManager().addActivity(this);
    ButterKnife.bind(this);
    mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);
    initTitle();
    StatusBarCompat.compat(this, Color.parseColor("#161414"));

    Intent intent = getIntent();
    mUpdateDiaryTvDate.setText("今天，" + GetDate.getDate());
    mUpdateDiaryEtTitle.setText(intent.getStringExtra("title"));
    mUpdateDiaryEtContent.setText(intent.getStringExtra("content"));
    mTvTag.setText(intent.getStringExtra("tag"));


  }

  private void initTitle() {
    ActionBar actionBar = getSupportActionBar();
    actionBar.hide();
    mCommonTvTitle.setText("修改日记");
  }

  @OnClick({R.id.common_iv_back, R.id.update_diary_tv_date, R.id.update_diary_et_title, R.id.update_diary_et_content, R.id.update_diary_fab_back, R.id.update_diary_fab_add, R.id.update_diary_fab_delete})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.common_iv_back:
        DiaryActivity.startActivity(this);
      case R.id.update_diary_tv_date:
        break;
      case R.id.update_diary_et_title:
        break;
      case R.id.update_diary_et_content:
        break;
      case R.id.update_diary_fab_back:
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("确定要删除该日记吗？").setPositiveButton("确定", (dialog, which) -> {
          String tag = mTvTag.getText().toString();
          SQLiteDatabase dbDelete = mHelper.getWritableDatabase();
          dbDelete.delete("Diary", "tag = ?", new String[]{tag});
          DiaryActivity.startActivity(UpdateDiaryActivity.this);
        }).setNegativeButton("取消", (dialog, which) -> {
        }).show();
        break;
      case R.id.update_diary_fab_add:
        SQLiteDatabase dbUpdate = mHelper.getWritableDatabase();
        ContentValues valuesUpdate = new ContentValues();
        String title = mUpdateDiaryEtTitle.getText().toString();
        String content = mUpdateDiaryEtContent.getText().toString();
        String tag = mTvTag.getText().toString();
        valuesUpdate.put("title", title);
        valuesUpdate.put("content", content);
//        dbUpdate.update("Diary", valuesUpdate, "title = ?", new String[]{title});
//        dbUpdate.update("Diary", valuesUpdate, "content = ?", new String[]{content});
        dbUpdate.update("Diary", valuesUpdate, "tag = ?", new String[]{tag});
        DiaryActivity.startActivity(this);
        break;
      case R.id.update_diary_fab_delete:
        DiaryActivity.startActivity(this);

        break;
    }
  }

  @OnClick(R.id.common_tv_title)
  public void onClick() {
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    DiaryActivity.startActivity(this);
  }
}