package cn.edu.zucc.bigapp.news.model;

import cn.edu.zucc.bigapp.news.bean.NewsBean;

public interface IOnLoadListener {
  void success(NewsBean newsBean);

  void fail(String error);
}