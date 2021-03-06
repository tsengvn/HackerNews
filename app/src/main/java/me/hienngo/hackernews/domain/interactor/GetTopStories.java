package me.hienngo.hackernews.domain.interactor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.hienngo.hackernews.AppConfig;
import me.hienngo.hackernews.domain.repo.CacheRepo;
import me.hienngo.hackernews.domain.repo.HackerNewsRepo;
import me.hienngo.hackernews.model.Item;
import me.hienngo.hackernews.model.StoryModel;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * @author hienngo
 * @since 9/29/17
 */

public class GetTopStories {
    private final HackerNewsRepo hackerNewsRepo;
    private final CacheRepo cacheRepo;
    private final AppConfig appConfig;

    private List<Long> ids;
    private int current = 0;
    private List<StoryModel> currentStoryList;

    public GetTopStories(HackerNewsRepo hackerNewsRepo, CacheRepo cacheRepo, AppConfig appConfig) {
        this.hackerNewsRepo = hackerNewsRepo;
        this.cacheRepo = cacheRepo;
        this.appConfig = appConfig;
        this.ids = new ArrayList<>();
        this.currentStoryList = new ArrayList<>();
        this.current = 0;
    }

    public Observable<List<StoryModel>> getTopStories(boolean refresh) {
        if (refresh) {
            clearCurrentData();
        }

        if (this.ids.isEmpty()) {
            return hackerNewsRepo.getTopStories()
                    .subscribeOn(Schedulers.io())
                    .flatMap(ids -> {
                            this.ids.addAll(ids);
                            return mapIdsToStories();
                        }
                    );
        } else {
            return Observable.just(currentStoryList);
        }
    }

    void clearCurrentData() {
        current = 0;
        this.ids.clear();
        currentStoryList.clear();
        cacheRepo.evictAll();
    }

    private Observable<List<StoryModel>> mapIdsToStories() {
        return Observable.from(getIdsToFetch(0))
                .observeOn(Schedulers.io())
                .map(this::getItem)
                .filter(item -> item != null)
                .map(StoryModel::new)
                .toList()
                .doOnNext(storyModels -> currentStoryList.addAll(storyModels));
    }

    public Observable<List<StoryModel>> loadNext() {
        return Observable.from(getIdsToFetch(current))
                .observeOn(Schedulers.io())
                .map(this::getItem)
                .filter(item -> item != null)
                .map(StoryModel::new)
                .toList()
                .doOnNext(storyModels -> currentStoryList.addAll(storyModels));

    }

    private List<Long> getIdsToFetch(int fromIndex) {
        if (current >= ids.size()) {
            //no more item to fetch
            return new ArrayList<>();
        } else if (fromIndex == 0 && current > 0) {
            return ids.subList(fromIndex, current);
        } else {
            current = (current + appConfig.getStoryPageItem() < ids.size()) ? current + appConfig.getStoryPageItem() : ids.size();
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
