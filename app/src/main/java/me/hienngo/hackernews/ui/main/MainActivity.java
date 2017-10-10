package me.hienngo.hackernews.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.hienngo.hackernews.R;
import me.hienngo.hackernews.di.component.AppComponent;
import me.hienngo.hackernews.domain.interactor.GetTopStories;
import me.hienngo.hackernews.model.StoryModel;
import me.hienngo.hackernews.ui.base.BaseActivity;
import me.hienngo.hackernews.ui.base.ItemClickListener;
import me.hienngo.hackernews.ui.base.LoadMoreListener;
import me.hienngo.hackernews.ui.comment.CommentActivity;
import me.hienngo.hackernews.util.ScrollUtil;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView, LoadMoreListener, ItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    GetTopStories getTopStories;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayoutManager layoutManager;
    private Bundle savedState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout.setOnRefreshListener(this);
    }


    @Override
    public void initInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter(getTopStories);
    }

    @Override
    public void onReceivedData(List<StoryModel> itemList, boolean isLoadMore) {
        StoryAdapter storyAdapter;
        if (recyclerView.getAdapter() == null) {
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, OrientationHelper.VERTICAL));
            storyAdapter = new StoryAdapter(this);
            storyAdapter.setLoadMoreListener(this);
            storyAdapter.setClickListener(this);

            recyclerView.setAdapter(storyAdapter);

        } else {
            storyAdapter = (StoryAdapter) recyclerView.getAdapter();
        }

        storyAdapter.setDataList(itemList, isLoadMore);

        if (savedState != null) {
            ScrollUtil.restoreLastScrollPosition(recyclerView, savedState);
            savedState = null;
        }
    }

    @Override
    public void loadMore() {
        getPresenter().loadMore();
    }

    @Override
    public void showError(String message) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void dismissLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ScrollUtil.saveCurrentScrollPosition(recyclerView, outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            savedState = savedInstanceState;
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onItemClicked(long itemId, String title) {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra("itemId", itemId);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        getPresenter().refresh();
    }
}
