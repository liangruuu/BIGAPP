package cn.edu.zucc.bigapp.news.model;

import cn.edu.zucc.bigapp.news.api.Api;
import cn.edu.zucc.bigapp.news.api.RetrofitHelper;
import cn.edu.zucc.bigapp.news.bean.NewsBean;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsModel implements INewsModel {
  @Override
  public void loadNews(final String type,
                       final IOnLoadListener iOnLoadListener) {
    RetrofitHelper retrofitHelper = new RetrofitHelper(Api.NEWS_HOST);
    retrofitHelper.getNews(type).enqueue(new Callback<NewsBean>() {
      @Override
      public void onResponse(Call<NewsBean> call, Response<NewsBean> response) {
        if (response.isSuccessful()) {
          iOnLoadListener.success(response.body());
        } else {
          iOnLoadListener.fail("");
        }
      }

      @Override
      public void onFailure(Call<NewsBean> call, Throwable t) {
        iOnLoadListener.fail(t.toString());
      }
    });
  }
}
