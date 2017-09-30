package me.hienngo.hackernews.ui.base;

/**
 * @author hienngo
 * @since 9/29/17
 */

public abstract class BasePresenter<V extends BaseView> {
    private V view;

    public abstract void onViewReady();

    public abstract void onViewDestroyed();

    public void attachView(V view) {
        this.view = view;
        onViewReady();
    }

    public void detachView() {
        onViewDestroyed();
        this.view = null;
    }

    public V getView() {
        return view;
    }
}
