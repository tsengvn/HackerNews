package me.hienngo.hackernews.ui.main;

import me.hienngo.hackernews.domain.interactor.GetTopStories;
import me.hienngo.hackernews.ui.base.BasePresenter;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * @author hienngo
 * @since 9/30/17
 */

public class MainPresenter extends BasePresenter<MainView>{
    private final GetTopStories getTopStories;

    Subscription subscription;

    public MainPresenter(GetTopStories getTopStories) {
        this.getTopStories = getTopStories;
    }

    @Override
    public void onViewReady() {
        getTopStories(false);
    }

    @Override
    public void onViewDestroyed() {
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
        getView().dismissLoading();
    }

    public void refresh() {
        getTopStories(true);
    }

    public void loadMore() {
        if (subscription == null || subscription.isUnsubscribed()) {
            getView().showLoading();
            subscription = getTopStories.loadNext()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(items -> {
                        getView().onReceivedData(items, true);
                        getView().dismissLoading();
                    }, this::onError);

        }
    }

    private void onError(Throwable throwable) {
        getView().dismissLoading();
        getView().showError(throwable.getMessage());
    }

    void getTopStories(boolean refresh) {
        getView().showLoading();
        subscription = getTopStories.getTopStories(refresh)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
                    getView().onReceivedData(items, false);
                    getView().dismissLoading();
                }, this::onError);
    }

}
