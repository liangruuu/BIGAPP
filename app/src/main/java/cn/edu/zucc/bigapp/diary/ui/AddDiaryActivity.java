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


public class AddDiaryActivity extends AppCompatActivity {

  @BindView(R.id.add_diary_tv_date)
  TextView mAddDiaryTvDate;
  @BindView(R.id.add_diary_et_title)
  EditText mAddDiaryEtTitle;
  @BindView(R.id.add_diary_et_content)
  LinedEditText mAddDiaryEtContent;
  @BindView(R.id.add_diary_fab_back)
  FloatingActionButton mAddDiaryFabBack;
  @BindView(R.id.add_diary_fab_add)
  FloatingActionButton mAddDiaryFabAdd;

  @BindView(R.id.right_labels)
  FloatingActionsMenu mRightLabels;
  @BindView(R.id.common_tv_title)
  TextView mCommonTvTitle;
  @BindView(R.id.common_title_ll)
  LinearLayout mCommonTitleLl;
  @BindView(R.id.common_iv_back)
  ImageView mCommonIvBack;

  private DiaryDatabaseHelper mHelper;

  public static void startActivity(Context context) {
    Intent intent = new Intent(context, AddDiaryActivity.class);
    context.startActivity(intent);
  }

//  public static void startActivity(Context context, String title, String content) {
//    Intent intent = new Intent(context, AddDiaryActivity.class);
//    intent.putExtra("title", title);
//    intent.putExtra("content", content);
//    context.startActivity(intent);
//  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_diary);
    AppManager.getAppManager().addActivity(this);
    ButterKnife.bind(this);
    ActionBar actionBar = getSupportActionBar();
    actionBar.hide();
    Intent intent = getIntent();
    mAddDiaryEtTitle.setText(intent.getStringExtra("title"));
    StatusBarCompat.compat(this, Color.parseColor("#161414"));

    mCommonTvTitle.setText("添加日记");
    mAddDiaryTvDate.setText("今天，" + GetDate.getDate());
    mAddDiaryEtContent.setText(intent.getStringExtra("content"));
    mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);
  }


  @OnClick({R.id.common_iv_back, R.id.add_diary_et_title, R.id.add_diary_et_content, R.id.add_diary_fab_back, R.id.add_diary_fab_add})
  public void onClick(View view) {
    String tag = String.valueOf(System.currentTimeMillis());
    switch (view.getId()) {
      case R.id.common_iv_back:
        DiaryActivity.startActivity(this);
      case R.id.add_diary_et_title:
        break;
      case R.id.add_diary_et_content:
        break;
      case R.id.add_diary_fab_back:
        String date = GetDate.getDate().toString();

        String title = mAddDiaryEtTitle.getText().toString() + "";
        String content = mAddDiaryEtContent.getText().toString() + "";
        if (!title.equals("") || !content.equals("")) {
          SQLiteDatabase db = mHelper.getWritableDatabase();
          ContentValues values = new ContentValues();
          values.put("date", date);
          values.put("title", title);
          values.put("content", content);
          values.put("tag", tag);
          db.insert("Diary", null, values);
          values.clear();
        }
        DiaryActivity.startActivity(this);
        break;
      case R.id.add_diary_fab_add:
        final String dateBack = GetDate.getDate().toString();
        final String titleBack = mAddDiaryEtTitle.getText().toString();
        final String contentBack = mAddDiaryEtContent.getText().toString();
        if (!titleBack.isEmpty() || !contentBack.isEmpty()) {
          AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
          alertDialogBuilder.setMessage("是否保存日记内容？").setPositiveButton("确定", (dialog, which) -> {
            SQLiteDatabase db = mHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("date", dateBack);
            values.put("title", titleBack);
            values.put("content", contentBack);
            values.put("tag", tag);
            db.insert("Diary", null, values);
            values.clear();
            DiaryActivity.startActivity(AddDiaryActivity.this);
          }).setNegativeButton("取消",
              (dialog, which) -> DiaryActivity.startActivity(AddDiaryActivity.this)).show();
        } else {
          DiaryActivity.startActivity(this);
        }
        break;
    }
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    DiaryActivity.startActivity(this);
  }
}











