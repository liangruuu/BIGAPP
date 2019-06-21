package cn.edu.zucc.bigapp.news.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zucc.bigapp.R;
import cn.edu.zucc.bigapp.news.bean.NewsBean;

public class ItemNewsAdapter extends RecyclerView.Adapter<ItemNewsAdapter.ItemNewsHolder> {

  private List<NewsBean.Bean> objects = new ArrayList<>();

  private Context context;

  public ItemNewsAdapter(Context context) {
    this.context = context;
  }

  public void setData(List<NewsBean.Bean> objects) {
    this.objects = objects;
  }

  @NonNull
  @Override
  public ItemNewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_news, parent, false);
    return new ItemNewsHolder(view);
  }


  @Override
  public void onBindViewHolder(@NonNull ItemNewsHolder itemNewsHolder, int i) {
    final NewsBean.Bean bean = objects.get(i);
    if (bean == null) {
      return;
    }
    Glide.with(context)
        .load(bean.getPicUrl())
        .into(itemNewsHolder.ivNewsImg);
    itemNewsHolder.tvNewsTitle.setText(bean.getTitle());
    itemNewsHolder.cvNews.setOnClickListener(view -> {
      Intent intent = new Intent(context, NewsDetail.class);
      intent.putExtra("url", bean.getUrl());
      intent.putExtra("title", bean.getTitle());
      context.startActivity(intent);
    });
  }

  @Override
  public int getItemCount() {
    return objects.size();
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  protected class ItemNewsHolder extends RecyclerView.ViewHolder {
    private ImageView ivNewsImg;
    private TextView tvNewsTitle;
//    private TextView tvNewsDesc;
    private CardView cvNews;

    public ItemNewsHolder(View view) {
      super(view);
      ivNewsImg = view.findViewById(R.id.iv_news_img);
      tvNewsTitle = view.findViewById(R.id.tv_news_title);
//      tvNewsDesc = view.findViewById(R.id.tv_news_desc);
      cvNews = view.findViewById(R.id.cv_news);
    }
  }

}
