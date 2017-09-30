package me.hienngo.hackernews.ui.main;

import java.util.List;

import me.hienngo.hackernews.domain.interactor.DataManager;
import me.hienngo.hackernews.modal.Item;
import me.hienngo.hackernews.ui.base.BasePresenter;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author hienngo
 * @since 9/30/17
 */

public class MainPresenter extends BasePresenter<MainView>{
    private final DataManager dataManager;

    private Subscription subscription;
    public MainPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void onViewReady() {
        subscription = dataManager.getTopStories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DataSubscriber());
    }

    @Override
    public void onViewDestroyed() {
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }

    private class DataSubscriber extends Subscriber<List<Item>>{
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<Item> items) {


        }
    }

}
