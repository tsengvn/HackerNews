package me.hienngo.hackernews.domain.interactor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.hienngo.hackernews.domain.repo.CacheRepo;
import me.hienngo.hackernews.domain.repo.HackerNewsRepo;
import me.hienngo.hackernews.model.Item;
import me.hienngo.hackernews.model.StoryModel;
import rx.Observable;

/**
 * @author hienngo
 * @since 9/29/17
 */

public class GetTopStories {
    private final HackerNewsRepo hackerNewsRepo;
    private final CacheRepo cacheRepo;
    private static final int PAGE_ITEM = 10;
    private List<Long> ids;
    private int current = 0;
    public GetTopStories(HackerNewsRepo hackerNewsRepo, CacheRepo cacheRepo) {
        this.hackerNewsRepo = hackerNewsRepo;
        this.cacheRepo = cacheRepo;
        this.ids = new ArrayList<>();
    }

    public Observable<List<StoryModel>> getTopStories() {
        current = 0;
        return hackerNewsRepo.getTopStories()
                .doOnNext(ids -> {
                    this.ids.clear();
                    this.ids.addAll(ids);
                })
                .flatMap(ids -> Observable.from(getIdsToFetch()))
                .map(this::getItem)
                .filter(item -> item != null)
                .map(StoryModel::new)
                .toList()
                .doOnNext(list -> current+= PAGE_ITEM);
    }

    public Observable<List<StoryModel>> loadNext() {
        return Observable.from(getIdsToFetch())
                .map(this::getItem)
                .filter(item -> item != null)
                .map(StoryModel::new)
                .toList()
                .doOnNext(list -> current+= PAGE_ITEM);

    }

    private List<Long> getIdsToFetch() {
        if (current >= ids.size()) return new ArrayList<>();

        int toIndex = (current + PAGE_ITEM < ids.size()) ? current + PAGE_ITEM : ids.size()-1;
        return ids.subList(current, toIndex);
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


}
