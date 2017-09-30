package me.hienngo.hackernews.ui.main;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

public class MainActivity extends BaseActivity<MainPresenter> implements MainView, StoryAdapter.LoadMoreListener {

    @Inject
    GetTopStories getTopStories;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Snackbar loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(new DividerItemDecoration(this, OrientationHelper.VERTICAL));
            storyAdapter = new StoryAdapter(this);
            storyAdapter.setListener(this);
            recyclerView.setAdapter(storyAdapter);
        } else {
            storyAdapter = (StoryAdapter) recyclerView.getAdapter();
        }

        storyAdapter.setDataList(itemList, isLoadMore);

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
        loading = Snackbar.make(recyclerView, "Loading", Snackbar.LENGTH_INDEFINITE);
        loading.show();
    }

    @Override
    public void dismissLoading() {
        if (loading != null) {
            loading.dismiss();
            loading = null;
        }
    }
}
