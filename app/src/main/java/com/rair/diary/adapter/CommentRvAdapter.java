package com.rair.diary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rair.diary.R;
import com.rair.diary.bean.Comment;
import com.rair.diary.bean.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Rair on 2017/6/25.
 * Email:rairmmd@gmail.com
 * Author:Rair
 */

public class CommentRvAdapter extends RecyclerView.Adapter<CommentRvAdapter.CommentHolder> {

    private Context context;
    private ArrayList<Comment> datas;
    public CommentRvAdapter(Context context,ArrayList<Comment> data) {
        this.context = context;
        this.datas = data;
    }
    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_comment_item, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        User user = datas.get(position).getUser();
        if (user.getSex() != null) {
            if (user.getSex() == 0) {
                Picasso.with(context).load(R.mipmap.male).into(holder.sexView);
            } else {
                Picasso.with(context).load(R.mipmap.female).into(holder.sexView);
            }
        }
        if (user.getHeadFile() != null) {
            BmobFile headFileFile = user.getHeadFile();
            Picasso.with(context).load(headFileFile.getFileUrl()).into(holder.headView);
        } else {
            Picasso.with(context).load(R.mipmap.ic_head).into(holder.headView);
        }
        holder.commentName.setText( user.getNickName());
        holder.commentTime.setText(datas.get(position).getCommentTime());
        holder.commentContent.setText(datas.get(position).getContent());

    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder {
        private TextView commentName, commentTime, commentContent;
        private ImageView sexView, headView;
        CommentHolder(View itemView) {
            super(itemView);
            commentName = (TextView) itemView.findViewById(R.id.comment_item_tv_name);
            commentTime = (TextView) itemView.findViewById(R.id.comment_item_tv_time);
            commentContent = (TextView) itemView.findViewById(R.id.comment_item_tv_content);
            sexView = (ImageView) itemView.findViewById(R.id.comment_item_iv_sex);
            headView = (ImageView) itemView.findViewById(R.id.comment_item_civ_head);
        }
    }
}
