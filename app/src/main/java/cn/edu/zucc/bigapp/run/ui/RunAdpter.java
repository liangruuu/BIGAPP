package cn.edu.zucc.bigapp.run.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.zucc.bigapp.R;
import cn.edu.zucc.bigapp.run.bean.RunBean;


public class RunAdpter extends RecyclerView.Adapter<RunAdpter.RunViewHolder> {

  private Context mContext;
  private LayoutInflater mLayoutInflater;
  private List<RunBean> mRunBeanList;

  public RunAdpter(Context context, List<RunBean> mRunBeanList) {
    mContext = context;
    this.mLayoutInflater = LayoutInflater.from(context);
    this.mRunBeanList = mRunBeanList;
  }

  @Override
  public RunViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new RunViewHolder(mLayoutInflater.inflate(R.layout.item_run, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull RunViewHolder holder, int i) {
    holder.mTvDate.setText(mRunBeanList.get(i).getDate());
    holder.runHistoryDistance.setText(mRunBeanList.get(i).getDistance());
    holder.runHistoryTime.setText(mRunBeanList.get(i).getTime());
  }

  @Override
  public int getItemCount() {
    return mRunBeanList.size();
  }

  static class RunViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.main_iv_circle)
    ImageView mIvCircle;
    @BindView(R.id.main_tv_date)
    TextView mTvDate;
    @BindView(R.id.run_history_distance)
    TextView runHistoryDistance;
    @BindView(R.id.run_history_time)
    TextView runHistoryTime;

    RunViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }
}
