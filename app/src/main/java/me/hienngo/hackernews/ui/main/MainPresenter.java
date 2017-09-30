package me.hienngo.hackernews.ui.main;

import me.hienngo.hackernews.domain.interactor.GetTopStories;
import me.hienngo.hackernews.ui.base.BasePresenter;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author hienngo
 * @since 9/30/17
 */

public class MainPresenter extends BasePresenter<MainView>{
    private final GetTopStories getTopStories;

    private Subscription subscription;
    public MainPresenter(GetTopStories getTopStories) {
        this.getTopStories = getTopStories;
    }

    @Override
    public void onViewReady() {
        getView().showLoading();
        subscription = getTopStories.getTopStories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
                    getView().onReceivedData(items, false);
                    getView().dismissLoading();
                }, this::onError);
    }

    @Override
    public void onViewDestroyed() {
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }

    public void loadMore() {
        if (subscription == null || subscription.isUnsubscribed()) {
            getView().showLoading();
            subscription = getTopStories.loadNext()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(items -> {
                        getView().onReceivedData(items, true);
                        getView().dismissLoading();
                    }, this::onError);

        }
    }

    private void onError(Throwable throwable) {
        throwable.printStackTrace();
        getView().showError(throwable.getMessage());
    }
}
