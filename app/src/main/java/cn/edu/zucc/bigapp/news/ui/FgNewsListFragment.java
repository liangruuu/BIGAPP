package cn.edu.zucc.bigapp.news.ui;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.List;
import java.util.TimerTask;

import cn.edu.zucc.bigapp.R;
import cn.edu.zucc.bigapp.news.bean.NewsBean;
import cn.edu.zucc.bigapp.news.presenter.NewsPresenter;
import cn.edu.zucc.bigapp.news.view.INewsView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FgNewsListFragment extends Fragment implements INewsView {

  private NewsPresenter presenter;
  private int type;
  private RecyclerView rvNews;
  private SwipeRefreshLayout srlNews;
  private ItemNewsAdapter adapter;
  private List<NewsBean.Bean> newsBeanList;
  private TextView tvNewsList;

  public static FgNewsListFragment newInstance(int type) {
    Bundle bundle = new Bundle();
    FgNewsListFragment fragment = new FgNewsListFragment();
    bundle.putInt("type", type);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_fg_news_list, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    type = getArguments().getInt("type");

    srlNews = view.findViewById(R.id.srl_news);
    rvNews = view.findViewById(R.id.rv_news);
    tvNewsList = view.findViewById(R.id.tv_news_list);

    srlNews.setColorSchemeColors(Color.parseColor("#ffce3d3a"));
    adapter = new ItemNewsAdapter(getActivity());
    presenter = new NewsPresenter(this);

    srlNews.post(() -> {
      showDialog();
      presenter.loadNews(type, 0);
    });

    srlNews.setOnRefreshListener(() -> presenter.loadNews(type, 0));

  }

  @Override
  public void showNews(NewsBean newsBean) {
    getActivity().runOnUiThread(new TimerTask() {
      @Override
      public void run() {
        newsBeanList = newsBean.getNewslist();
        adapter.setData(newsBeanList);
        rvNews.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvNews.setAdapter(adapter);
        tvNewsList.setVisibility(View.GONE);
      }
    });
  }

  @Override
  public void hideDialog() {
    srlNews.setRefreshing(false);
  }

  @Override
  public void showDialog() {
    srlNews.setRefreshing(true);
  }

  @Override
  public void showErrorMsg(String error) {
    tvNewsList.setText("加载失败:" + error);
  }
}
