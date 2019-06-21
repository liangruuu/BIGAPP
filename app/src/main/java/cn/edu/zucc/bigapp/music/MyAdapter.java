package cn.edu.zucc.bigapp.music;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zucc.bigapp.R;
import cn.edu.zucc.bigapp.music.model.Music;
import cn.edu.zucc.bigapp.music.model.staticData;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


  List<Music> listitem = new ArrayList<Music>();
  private Context context;

  public MyAdapter() {
  }

  public MyAdapter(List<Music> list, Context context) {

    this.listitem = list;
    this.context = context;
  }

  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    MyViewHolder holder = new MyViewHolder(

        LayoutInflater.from(context).inflate(R.layout.list_item, null, false));
    return holder;
  }

  @Override
  public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder myViewHolder, int i) {
    Music item = listitem.get(i);
    item.setId(i);
    final int id = i;
    myViewHolder.tv2.setText(item.getDuration());
    myViewHolder.tv3.setText(item.getTitle());
    myViewHolder.tv4.setText(item.getArtist());
    myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent it = new Intent(context, MusicActivity.class);
        staticData.index = id;
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
      }
    });


  }

  @Override
  public int getItemCount() {
    return this.listitem.size();
  }

  class MyViewHolder extends RecyclerView.ViewHolder {

    TextView tv2;
    TextView tv3;
    TextView tv4;
    ImageView imageView;


    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
      imageView = (ImageView) itemView.findViewById(R.id.item_img);
      tv2 = (TextView) itemView.findViewById(R.id.item_musictime);
      tv3 = (TextView) itemView.findViewById(R.id.item_musicname);
      tv4 = (TextView) itemView.findViewById(R.id.item_musicartist);


    }
  }
}

