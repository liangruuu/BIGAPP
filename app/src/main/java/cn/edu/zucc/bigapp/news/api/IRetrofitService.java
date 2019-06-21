package cn.edu.zucc.bigapp.news.api;

import cn.edu.zucc.bigapp.news.bean.NewsBean;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IRetrofitService {

  /**
   * http://c.m.163.com/news/t/T1348647909107/0-20.html
   */

//  @GET("nc/article/{type}/{id}/{startPage}-20.html")
//  Call<NewsBean> getNews(@Path("type") String type,
//                         @Path("id") String id,
//                         @Path("startPage") int startPage);
  @GET("/{type}")
  Call<NewsBean> getNews(@Path("type") String type,
                         @Query("key") String key,
                         @Query("num") int num);
}
