package cn.edu.zucc.bigapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.zucc.bigapp.news.api.IRetrofitService;
import cn.edu.zucc.bigapp.news.bean.NewsBean;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HTTPTESTActivity extends AppCompatActivity {

  private final String appKey = "6486017f9e62d4aa0b908aae6988ce2f";
  private static String TAG = "jianglr";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_httptest);

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://api.tianapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    IRetrofitService retrofitService = retrofit.create(IRetrofitService.class);
    Call<NewsBean> call = retrofitService.getNews("social", appKey, 10);

    TextView textView = findViewById(R.id.test);


    call.enqueue(new Callback<NewsBean>() {
      @Override
      public void onResponse(Call<NewsBean> call, Response<NewsBean> response) {
        if (response.body() != null) {
          textView.setText(response.body().getNewslist().get(0).getUrl());
        } else {
          textView.setText("错误");
        }
        Log.i(TAG, "onResponse: " + call.request().url());
      }

      @Override
      public void onFailure(Call<NewsBean> call, Throwable t) {
        Toast.makeText(HTTPTESTActivity.this, "请求失败" + call.request().url(), Toast.LENGTH_SHORT).show();
        Log.i(TAG, "请求失败" + call.request().url());
      }
    });

  }
}
