package cn.edu.zucc.bigapp.news.model;

public interface INewsModel {
  //  void loadNews(String Type, int startPage, String id, IOnLoadListener iNewsLoadListener);
  void loadNews(String Type, IOnLoadListener iNewsLoadListener);
}