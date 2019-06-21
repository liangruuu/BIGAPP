package cn.edu.zucc.bigapp.news.view;

import cn.edu.zucc.bigapp.news.bean.NewsBean;

public interface INewsView {
  void showNews(NewsBean newsBean);

  void hideDialog();

  void showDialog();

  void showErrorMsg(String error);
}
