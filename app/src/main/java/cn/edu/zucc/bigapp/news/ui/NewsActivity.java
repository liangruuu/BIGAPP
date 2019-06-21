package cn.edu.zucc.bigapp.news.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.zucc.bigapp.R;
import cn.edu.zucc.bigapp.diary.ui.DiaryActivity;

public class NewsActivity extends AppCompatActivity {

  @BindView(R.id.tl_news)
  TabLayout tlNews;
  @BindView(R.id.vp_news)
  ViewPager vpNews;
  @BindView(R.id.common_tv_title)
  TextView commonTvTitle;


  public static final int NEWS_TYPE_SOCIAL = 0;
  public static final int NEWS_TYPE_SPORT = 1;
  public static final int NEWS_TYPE_TECH = 2;

  private List<Fragment> fragments = new ArrayList<>();
  private List<String> fragmentTitles = new ArrayList<>();

  public static void startActivity(Context context) {
    Intent intent = new Intent(context, NewsActivity.class);
    context.startActivity(intent);
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_news);
    ButterKnife.bind(this);

    ActionBar actionBar = getSupportActionBar();
    actionBar.hide();

    initTitle();
    setViewPager();
    vpNews.setOffscreenPageLimit(2);
    tlNews.setupWithViewPager(vpNews);
  }

  private void setViewPager() {
    fragments.add(FgNewsListFragment.newInstance(NEWS_TYPE_SOCIAL));
    fragments.add(FgNewsListFragment.newInstance(NEWS_TYPE_SPORT));
    fragments.add(FgNewsListFragment.newInstance(NEWS_TYPE_TECH));
    fragmentTitles.add("社会");
    fragmentTitles.add("体育");
    fragmentTitles.add("科技");

    MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments, fragmentTitles);

    vpNews.setAdapter(adapter);
  }

  private void initTitle() {
    commonTvTitle.setText("资讯");
  }

  @OnClick({R.id.common_iv_back})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.common_iv_back:
        DiaryActivity.startActivity(this);
        break;
    }
  }
}
