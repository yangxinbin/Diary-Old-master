package com.rair.diary.ui.find;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rair.diary.R;
import com.rair.diary.adapter.FindXrvAdapter;
import com.rair.diary.bean.Diary;
import com.rair.diary.bean.User;
import com.rair.diary.utils.RairUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindFragment extends Fragment implements FindXrvAdapter.OnRvItemClickListener, XRecyclerView.LoadingListener,View.OnClickListener {

    @BindView(R.id.find_rv_list)
    XRecyclerView findrvList;
    Unbinder unbinder;
    @BindView(R.id.find_tv_tip)
    TextView findTvTip;
    private ArrayList<Diary> datas;
    //当前页
    private int pageNum = 0;
    private FindXrvAdapter findXrvAdapter;
    private TextView findTvfootTip;
    public static FindFragment newInstance() {
        FindFragment findFragment = new FindFragment();
        return findFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initView();
    }

    private void initView() {
        View footerView = getActivity().getLayoutInflater().inflate(R.layout.view_find_footer, new LinearLayout(getActivity()), false);
        //View headerView = getActivity().getLayoutInflater().inflate(R.layout.view_rv_header, null);
        findTvfootTip = (TextView) footerView.findViewById(R.id.find_tv_find_tip);
        findTvfootTip.setOnClickListener(this);
        datas = new ArrayList<>();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        findrvList.setLayoutManager(layoutManager);
        findXrvAdapter = new FindXrvAdapter(getContext(),datas);
        //findrvList.addHeaderView(headerView);
        findrvList.setFootView(footerView);
        findrvList.setAdapter(findXrvAdapter);
        findrvList.setLoadingListener(this);
       // findrvList.setLoadingMoreEnabled(true);
       // findrvList.setPullRefreshEnabled(true);
        //findXrvAdapter.setOnItemClickListener(this);
        findXrvAdapter.setOnRvItemClickListener(this);
        findrvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (totalItemCount <= (lastVisibleItemPosition + 6)) {
                    Log.v("yxb","----setVisibility---VISIBLE---");
                    findTvfootTip.setVisibility(View.VISIBLE);
                    //loadComments();
                } else {
                    Log.v("yxb","----setVisibility---GONE---");
                    findTvfootTip.setVisibility(View.GONE);
                }
            }
        });
        loadDiary();
    }

    /**
     * 加载日记
     */
    private void loadDiary() {
        BmobQuery<Diary> query = new BmobQuery<>();
        query.order("-createdAt");
        query.setLimit(6);
        query.setSkip(6 * (pageNum++));
        query.include("user");
        query.findObjects(new FindListener<Diary>() {
            @Override
            public void done(List<Diary> list, BmobException e) {
                if (e == null) {
                    findXrvAdapter.notifyDataSetChanged();
                    datas.addAll(list);
                    if (datas.size() == 0) {
                        findTvTip.setVisibility(View.VISIBLE);
                    } else {
                        findTvTip.setVisibility(View.GONE);
                    }
                    if (list.size() != 0 && list.get(list.size() - 1) != null) {
                        findTvfootTip.setText("更多分享");
                        if (list.size() < 6) {
                            findTvfootTip.setText("暂无更多分享~");
                        }
                    } else {
                        findTvfootTip.setText("暂无更多分享~");
                        pageNum--;
                    }
                    findXrvAdapter.notifyDataSetChanged();
                } else {
                    pageNum--;
                    RairUtils.showSnackar(findrvList, "加载失败，请稍后重试。");
                }
            }
        });
    }

//    @Override
//    public void onRefresh() {
//        datas.clear();
//        pageNum = 0;
//        loadDiary();
//    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) loadDiary();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        datas.clear();
        Log.v("yxb","----onLoadMore------"+pageNum);
        pageNum = 0;
        loadDiary();
        findrvList.refreshComplete();
    }

    @Override
    public void onLoadMore() {
        Log.v("yxb","----onLoadMore------"+pageNum);
        //loadDiary();
        //findrvList.loadMoreComplete();
    }

    @Override
    public void OnItemClick(int position) {
        Diary diary = datas.get(position);
        User user = diary.getUser();
        Intent intent = new Intent(getContext(), FindDetailActivity.class);
        intent.putExtra("title", diary.getTitle());
        intent.putExtra("content", diary.getContent());
        if (diary.getImage() != null)
            intent.putExtra("image", diary.getImage().getFileUrl());
        intent.putExtra("date", diary.getDate());
        intent.putExtra("week", diary.getWeek());
        intent.putExtra("weather", diary.getWeather());
        if (user.getHeadFile() != null)
            intent.putExtra("head", user.getHeadFile().getFileUrl());
        intent.putExtra("name", diary.getName());
        intent.putExtra("sex", user.getSex());
        intent.putExtra("sign", user.getSign());
        intent.putExtra("publish", diary.getCreateTime());
        intent.putExtra("diary", diary);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.find_tv_find_tip:
                loadDiary();
                break;
        }
    }
}
