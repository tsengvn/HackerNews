package me.hienngo.hackernews.domain.interactor;

import java.util.List;

import me.hienngo.hackernews.domain.repo.HackerNewsRepo;
import me.hienngo.hackernews.modal.Item;
import rx.Observable;

/**
 * @author hienngo
 * @since 9/29/17
 */

public class DataManager {
    private final HackerNewsRepo hackerNewsRepo;

    public DataManager(HackerNewsRepo hackerNewsRepo) {
        this.hackerNewsRepo = hackerNewsRepo;
    }

    public Observable<List<Item>> getTopStories() {
        return hackerNewsRepo.getTopStories()
                .flatMap(Observable::from)
                .map(hackerNewsRepo::getItemDetail)
                .toList();
    }
}
