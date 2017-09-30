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
        this.current = 0;
    }

    public Observable<List<StoryModel>> getTopStories() {
        if (this.ids.isEmpty()) {
            return hackerNewsRepo.getTopStories()
                    .flatMap(ids -> {
                        this.ids.addAll(ids);
                        return mapIdsToStories();
                        }
                    );
        } else {
            return mapIdsToStories();
        }
    }

    private Observable<List<StoryModel>> mapIdsToStories() {
        return Observable.from(getIdsToFetch(0))
                .map(this::getItem)
                .filter(item -> item != null)
                .map(StoryModel::new)
                .toList();
    }

    public Observable<List<StoryModel>> loadNext() {
        return Observable.from(getIdsToFetch(current))
                .map(this::getItem)
                .filter(item -> item != null)
                .map(StoryModel::new)
                .toList();

    }

    private List<Long> getIdsToFetch(int fromIndex) {
        if (current >= ids.size()) {
            //no more item to fetch
            return new ArrayList<>();
        } else if (fromIndex == 0 && current > 0) {
            return ids.subList(fromIndex, current);
        } else {
            current = (current + PAGE_ITEM < ids.size()) ? current + PAGE_ITEM : ids.size()-1;
            return ids.subList(fromIndex, current);
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


}
