package me.hienngo.hackernews.domain.interactor;

import com.annimon.stream.Stream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import me.hienngo.hackernews.domain.repo.CacheRepo;
import me.hienngo.hackernews.domain.repo.HackerNewsRepo;
import me.hienngo.hackernews.model.CommentModel;
import me.hienngo.hackernews.model.Item;
import rx.Observable;

/**
 * @author hienngo
 * @since 9/30/17
 */

public class GetStoryComments {
    private static final int PAGE_ITEM = 10;
    private final HackerNewsRepo hackerNewsRepo;
    private final CacheRepo cacheRepo;
    private final Stack<PendingItem> idStack;
    private List<CommentModel> dataList;

    private long currentStoryId;
    public GetStoryComments(HackerNewsRepo hackerNewsRepo, CacheRepo cacheRepo) {
        this.hackerNewsRepo = hackerNewsRepo;
        this.cacheRepo = cacheRepo;
        this.idStack = new Stack<>();
        this.dataList = new ArrayList<>();
    }

    public Observable<List<CommentModel>> loadCommentForStory(long storyId) {
        if (currentStoryId == storyId && dataList.size() > 0) {
            return Observable.just(dataList);
        } else {
            currentStoryId = storyId;
            dataList.clear();
            idStack.clear();
            return Observable.just(storyId)
                    .map(this::getItem)
                    .filter(item -> item != null)
                    .flatMap(item -> {
                        addToStack(item.kids, 1);
                        return loadCommentInStack();
                    })
                    .doOnNext(commentModels -> dataList.addAll(commentModels));
        }
    }

    public Observable<List<CommentModel>> loadNext() {
        return loadCommentInStack().doOnNext(commentModels -> dataList.addAll(commentModels));
    }

    private Observable<List<CommentModel>> loadCommentInStack() {
        return Observable.create(subscriber -> {
            List<CommentModel> result = new ArrayList<>();
            while (!idStack.isEmpty()) {
                PendingItem pendingItem = idStack.pop();
                Item comment = getItem(pendingItem.id);
                if (comment != null && !comment.dead && !comment.deleted) {
                    if (comment.kids != null) {
                        addToStack(comment.kids, pendingItem.level+1);
                    }

                    result.add(mapItemToComment(comment, pendingItem.level));
                }

                if (result.size() == PAGE_ITEM) {
                    break;
                }
            }

            if (subscriber != null && !subscriber.isUnsubscribed()) {
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        });
    }

    private CommentModel mapItemToComment(Item item, int level) {
        return new CommentModel(item, level);
    }

    private void addToStack(List<Long> ids, int level) {
        if (level == 1) {
            Stream.range(1, ids.size()).map(index -> ids.get(ids.size()-index))
                    .map(id -> new PendingItem(id, level)).forEach(idStack::push);
        } else {
            Stream.of(ids).map(id -> new PendingItem(id, level)).forEach(idStack::push);
        }
    }

    private Item getItem(long id){
        try {
            Item item = cacheRepo.getCache(id);

            if (item == null) {
                item = hackerNewsRepo.getItem(id).execute().body();
                cacheRepo.cacheItem(item);
            }
            return item;
        } catch (IOException e) {
            return null;
        }
    }

    private static class PendingItem {
        long id;
        int level;

        public PendingItem(long id, int level) {
            this.id = id;
            this.level = level;
        }
    }
}
