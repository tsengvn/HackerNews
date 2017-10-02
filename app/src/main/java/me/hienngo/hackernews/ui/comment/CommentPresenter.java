package me.hienngo.hackernews.ui.comment;

import me.hienngo.hackernews.domain.interactor.GetStoryComments;
import me.hienngo.hackernews.ui.base.BasePresenter;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author hienngo
 * @since 9/30/17
 */

class CommentPresenter extends BasePresenter<CommentView> {
    private final GetStoryComments getStoryComments;
    private final long itemId;

    Subscription subscription;
    CommentPresenter(GetStoryComments getStoryComments, long itemId) {
        this.getStoryComments = getStoryComments;
        this.itemId = itemId;
    }

    @Override
    public void onViewReady() {
        getView().showLoading();
        subscription = getStoryComments.loadCommentForStory(itemId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(commentModels -> {
                    getView().dismissLoading();
                    getView().onReceiveCommentData(commentModels, false);
                }, throwable -> {
//                    throwable.printStackTrace();
                    getView().dismissLoading();
                    getView().showError(throwable.getMessage());
                });
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
            subscription = getStoryComments.loadNext()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(commentModels -> {
                        getView().dismissLoading();
                        getView().onReceiveCommentData(commentModels, true);
                    }, throwable -> {
                        throwable.printStackTrace();
                        getView().dismissLoading();
                        getView().showError(throwable.getMessage());
                    });
        }
    }
}
