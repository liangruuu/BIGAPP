package cn.edu.zucc.bigapp.news.presenter;

import cn.edu.zucc.bigapp.news.api.Api;
import cn.edu.zucc.bigapp.news.bean.NewsBean;
import cn.edu.zucc.bigapp.news.model.INewsModel;
import cn.edu.zucc.bigapp.news.model.IOnLoadListener;
import cn.edu.zucc.bigapp.news.model.NewsModel;
import cn.edu.zucc.bigapp.news.ui.NewsActivity;
import cn.edu.zucc.bigapp.news.view.INewsView;

public class NewsPresenter implements INewsPresenter, IOnLoadListener {

  private INewsModel iNewsModel;
  private INewsView iNewsView;

  public NewsPresenter(INewsView iNewsView) {
    this.iNewsView = iNewsView;
    this.iNewsModel = new NewsModel();
  }

  @Override
  public void loadNews(int type, int startPage) {
    iNewsView.showDialog();
    switch (type) {
      case NewsActivity.NEWS_TYPE_SOCIAL:
        iNewsModel.loadNews(Api.SOCIAL_ID, this);
        break;
      case NewsActivity.NEWS_TYPE_SPORT:
        iNewsModel.loadNews(Api.SPORT_ID, this);
        break;
      case NewsActivity.NEWS_TYPE_TECH:
        iNewsModel.loadNews(Api.TECH_ID, this);
        break;
    }
  }

  @Override
  public void success(NewsBean newsBean) {
    iNewsView.hideDialog();
    if (newsBean != null) {
      iNewsView.showNews(newsBean);
    }
  }

  @Override
  public void fail(String error) {
    iNewsView.hideDialog();
    iNewsView.showErrorMsg(error);
  }
}
