package cn.edu.zucc.bigapp.run.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.zucc.bigapp.R;
import cn.edu.zucc.bigapp.diary.ui.DiaryActivity;

public class StartRunActivity extends AppCompatActivity {


  @BindView(R.id.common_iv_back)
  ImageView commonIvBack;
  @BindView(R.id.common_tv_title)
  TextView commonTvTitle;
  @BindView(R.id.common_title_ll)
  LinearLayout commonTitleLl;
  @BindView(R.id.run_start)
  Button runStart;
  @BindView(R.id.run_history)
  Button runHistory;

  public static void startActivity(Context context) {
    Intent intent = new Intent(context, StartRunActivity.class);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_start_run);
    ButterKnife.bind(this);

    ActionBar actionBar = getSupportActionBar();
    actionBar.hide();

    initTitle();
  }

  private void initTitle() {
    commonTvTitle.setText("跑步");
  }

  @OnClick({R.id.common_iv_back, R.id.run_start, R.id.run_history})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.common_iv_back:
        DiaryActivity.startActivity(this);
        break;
      case R.id.run_start:
        RunActivity.startActivity(this);
        break;
      case R.id.run_history:
        RunHistoryActivity.startActivity(this);
        break;
    }
  }
}
