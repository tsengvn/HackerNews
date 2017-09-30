package me.hienngo.hackernews.ui.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import me.hienngo.hackernews.HNApp;
import me.hienngo.hackernews.di.component.AppComponent;

/**
 * @author hienngo
 * @since 9/29/17
 */

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView{
    public abstract T createPresenter();
    public abstract void initInjection(AppComponent appComponent);

    private T presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjection(HNApp.appComponent(this));

        Object instance = getLastNonConfigurationInstance();
        if (instance != null) {
            try {
                presenter = (T) instance;
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }

        if (presenter == null) {
            presenter = createPresenter();
        }

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        if (presenter != null) presenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) presenter.detachView();
    }

    public T getPresenter() {
        return presenter;
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }
}
