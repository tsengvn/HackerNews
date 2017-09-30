package me.hienngo.hackernews.ui.comment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.hienngo.hackernews.R;
import me.hienngo.hackernews.di.component.AppComponent;
import me.hienngo.hackernews.domain.interactor.GetStoryComments;
import me.hienngo.hackernews.model.CommentModel;
import me.hienngo.hackernews.ui.base.BaseActivity;
import me.hienngo.hackernews.ui.base.LoadMoreListener;

/**
 * @author hienngo
 * @since 9/30/17
 */

public class CommentActivity extends BaseActivity<CommentPresenter> implements CommentView, LoadMoreListener {

    @Inject
    GetStoryComments getStoryComments;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;
    private Snackbar loading;
    private int lastIndex=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getIntent().getStringExtra("title"));
        }
    }

    @Override
    public CommentPresenter createPresenter() {
        return new CommentPresenter(getStoryComments, getIntent().getLongExtra("itemId", 0));
    }

    @Override
    public void initInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public void onReceiveCommentData(List<CommentModel> commentModelList, boolean loadMore) {
        CommentAdapter commentAdapter;
        if (recyclerView.getAdapter() == null) {
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, OrientationHelper.VERTICAL));
            commentAdapter = new CommentAdapter(this);
            commentAdapter.setLoadMoreListener(this);

            recyclerView.setAdapter(commentAdapter);

        } else {
            commentAdapter = (CommentAdapter) recyclerView.getAdapter();
        }

        commentAdapter.setDataList(commentModelList, loadMore);

        if (lastIndex != 0) {
            recyclerView.scrollToPosition(lastIndex);
            lastIndex = 0;
        }
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


    @Override
    public void loadMore() {
        getPresenter().loadMore();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (layoutManager != null) {
            outState.putInt("index", layoutManager.findFirstCompletelyVisibleItemPosition());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            lastIndex = savedInstanceState.getInt("index");
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}