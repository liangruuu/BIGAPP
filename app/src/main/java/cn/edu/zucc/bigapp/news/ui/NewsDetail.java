package cn.edu.zucc.bigapp.news.ui;

import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.edu.zucc.bigapp.R;

public class NewsDetail extends AppCompatActivity {

  private WebView wbNews;
  private String loadUrl, title;
  private WebViewClient webViewClient;
  private TextView tv_bar_title;
  private ImageView iv_back;
  private ProgressBar pb_load;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_news_detail);
    loadUrl = getIntent().getStringExtra("url");
    title = getIntent().getStringExtra("title");
    ActionBar actionBar = getSupportActionBar();
    actionBar.hide();
    initView();
    setWebViewClient();
  }

  private void setWebViewClient() {
    webViewClient = new WebViewClient() {
      @Override
      public void onPageStarted(WebView view, String url, Bitmap favicon) {
        pb_load.setVisibility(View.VISIBLE);
      }

      @Override
      public void onPageFinished(WebView view, String url) {
        pb_load.setVisibility(View.GONE);
      }
    };
    wbNews.setWebViewClient(webViewClient);
  }

  private void initView() {
    wbNews = findViewById(R.id.wb_news);
    wbNews.getSettings().setJavaScriptEnabled(true);
    wbNews.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    wbNews.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    wbNews.canGoBack();
    wbNews.canGoForward();
    wbNews.loadUrl(loadUrl);
    tv_bar_title = findViewById(R.id.tv_bar_title);
    tv_bar_title.setText(title);
    iv_back = findViewById(R.id.iv_back);
    iv_back.setOnClickListener(v -> finish());
    pb_load = findViewById(R.id.pb_load);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    wbNews.destroy();
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == event.KEYCODE_BACK && wbNews.canGoBack()) {
      wbNews.goBack();
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }
}
