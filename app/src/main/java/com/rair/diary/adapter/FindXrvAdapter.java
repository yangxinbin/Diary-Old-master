package com.rair.diary.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rair.diary.R;
import com.rair.diary.bean.Comment;
import com.rair.diary.bean.Diary;
import com.rair.diary.bean.User;
import com.rair.diary.view.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Rair on 2017/6/12.
 * Email:rairmmd@gmail.com
 * Author:Rair
 */
public class FindXrvAdapter extends XRecyclerView.Adapter<FindXrvAdapter.FinDiaryHolder> {

    private Context context;
    private ArrayList<Diary> datas;
    private FindXrvAdapter.OnRvItemClickListener onRvItemClickListener;

    public interface OnRvItemClickListener {
        void OnItemClick(int position);
    }

    public void setOnRvItemClickListener(FindXrvAdapter.OnRvItemClickListener onRvItemClickListener) {
        this.onRvItemClickListener = onRvItemClickListener;
    }
    public FindXrvAdapter(Context context,ArrayList<Diary> data) {
        this.context = context;
        this.datas = data;
    }
    @Override
    public FindXrvAdapter.FinDiaryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_find_item, parent, false);
        return new FinDiaryHolder(view);
    }

    @Override
    public void onBindViewHolder(FindXrvAdapter.FinDiaryHolder holder, int position) {
        User user = datas.get(position).getUser();
        if (user.getSex() == 0) {
            Picasso.with(context).load(R.mipmap.male).into(holder.sexFind);
        } else {
            Picasso.with(context).load(R.mipmap.female).into(holder.sexFind);
        }
        if (user.getHeadFile() != null) {
            BmobFile headFileFile = user.getHeadFile();
            Picasso.with(context).load(headFileFile.getFileUrl()).into(holder.headFind);
        } else {
            Picasso.with(context).load(R.mipmap.ic_head).into(holder.headFind);
        }
        final int mPosition = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRvItemClickListener != null)
                    onRvItemClickListener.OnItemClick(mPosition);
            }
        });
        holder.findName.setText( "来自："+user.getNickName());
        holder.findTime.setText("时间："+datas.get(position).getCreateTime());
        holder.findTitle.setText( datas.get(position).getTitle());
        holder.findContent.setText(datas.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class FinDiaryHolder extends RecyclerView.ViewHolder {
        private TextView findName, findTime, findTitle,findContent;
        private ImageView sexFind, headFind;
        FinDiaryHolder(View itemView) {
            super(itemView);
            findName = (TextView) itemView.findViewById(R.id.find_item_tv_name);
            findTime = (TextView) itemView.findViewById(R.id.find_item_tv_time);
            findTitle = (TextView) itemView.findViewById(R.id.find_item_tv_title);
            findContent = (TextView) itemView.findViewById(R.id.find_item_tv_content);
            sexFind = (ImageView) itemView.findViewById(R.id.find_item_iv_sex);
            headFind = (ImageView) itemView.findViewById(R.id.find_item_civ_head);
        }
    }
}
