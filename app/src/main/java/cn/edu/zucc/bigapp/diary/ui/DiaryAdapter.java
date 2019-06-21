package cn.edu.zucc.bigapp.diary.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.zucc.bigapp.R;
import cn.edu.zucc.bigapp.diary.bean.DiaryBean;
import cn.edu.zucc.bigapp.diary.event.StartUpdateDiaryEvent;
import cn.edu.zucc.bigapp.diary.utils.GetDate;


public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

  private Context mContext;
  private LayoutInflater mLayoutInflater;
  private List<DiaryBean> mDiaryBeanList;
  private int mEditPosition = -1;

  public DiaryAdapter(Context context, List<DiaryBean> mDiaryBeanList) {
    mContext = context;
    this.mLayoutInflater = LayoutInflater.from(context);
    this.mDiaryBeanList = mDiaryBeanList;
  }

  @Override
  public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new DiaryViewHolder(mLayoutInflater.inflate(R.layout.item_rv_diary, parent, false));
  }

  @Override
  public void onBindViewHolder(final DiaryViewHolder holder, final int position) {

    String dateSystem = GetDate.getDate().toString();
    if (mDiaryBeanList.get(position).getDate().equals(dateSystem)) {
      holder.mIvCircle.setImageResource(R.drawable.circle_orange);
    }
    holder.mTvDate.setText(mDiaryBeanList.get(position).getDate());
    holder.mTvTitle.setText(mDiaryBeanList.get(position).getTitle());
    holder.mTvContent.setText("       " + mDiaryBeanList.get(position).getContent());
    holder.mIvEdit.setVisibility(View.INVISIBLE);
    if (mEditPosition == position) {
      holder.mIvEdit.setVisibility(View.VISIBLE);
    } else {
      holder.mIvEdit.setVisibility(View.GONE);
    }
    holder.mLl.setOnClickListener(v -> {
      if (holder.mIvEdit.getVisibility() == View.VISIBLE) {
        holder.mIvEdit.setVisibility(View.GONE);
      } else {
        holder.mIvEdit.setVisibility(View.VISIBLE);
      }
      if (mEditPosition != position) {
        notifyItemChanged(mEditPosition);
      }
      mEditPosition = position;
    });

    holder.mIvEdit.setOnClickListener(v -> EventBus.getDefault().post(new StartUpdateDiaryEvent(position)));

  }

  @Override
  public int getItemCount() {
    return mDiaryBeanList.size();
  }

  static class DiaryViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.main_iv_circle)
    ImageView mIvCircle;
    @BindView(R.id.main_tv_date)
    TextView mTvDate;
    @BindView(R.id.main_tv_title)
    TextView mTvTitle;

    @BindView(R.id.main_tv_content)
    TextView mTvContent;
    @BindView(R.id.main_iv_edit)
    ImageView mIvEdit;
    @BindView(R.id.item_ll)
    LinearLayout mLl;

    DiaryViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

//  public static class DiaryViewHolder extends RecyclerView.ViewHolder {
//
//    TextView mTvDate;
//    TextView mTvTitle;
//    TextView mTvContent;
//    ImageView mIvEdit;
//    LinearLayout mLlTitle;
//    LinearLayout mLl;
//    ImageView mIvCircle;
//    LinearLayout mLlControl;
//    RelativeLayout mRlEdit;
//
//    DiaryViewHolder(View view) {
//      super(view);
//      mIvCircle = view.findViewById(R.id.main_iv_circle);
//      mTvDate = view.findViewById(R.id.main_tv_date);
//      mTvTitle = view.findViewById(R.id.main_tv_title);
//      mTvContent = view.findViewById(R.id.main_tv_content);
//      mIvEdit = view.findViewById(R.id.main_iv_edit);
//      mLlTitle = view.findViewById(R.id.main_ll_title);
//      mLl = view.findViewById(R.id.item_ll);
//      mLlControl = view.findViewById(R.id.item_ll_control);
//      mRlEdit = view.findViewById(R.id.item_rl_edit);
//    }
//  }
}