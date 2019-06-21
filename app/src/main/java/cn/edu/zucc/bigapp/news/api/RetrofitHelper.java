package cn.edu.zucc.bigapp.news.api;

import java.util.concurrent.TimeUnit;

import cn.edu.zucc.bigapp.news.bean.NewsBean;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static cn.edu.zucc.bigapp.news.api.Api.*;

public class RetrofitHelper {

  private static OkHttpClient okHttpClient;
  private IRetrofitService retrofitService;

  public RetrofitHelper(String host) {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(host)
        .client(getOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    retrofitService = retrofit.create(IRetrofitService.class);
  }

  public Call<NewsBean> getNews(String type) {
//    return retrofitService.getNews(type, id, startPage);
    return retrofitService.getNews(type, KEY, NUM);
  }

  public OkHttpClient getOkHttpClient() {
    if (okHttpClient == null) {
      okHttpClient = new OkHttpClient.Builder()
          .retryOnConnectionFailure(true)
          .connectTimeout(30, TimeUnit.SECONDS)
          .addInterceptor(chain -> {
            Request request = chain.request()
                .newBuilder()
                .build();
            return chain.proceed(request);
          })
          .build();

    }
    return okHttpClient;
  }

}
