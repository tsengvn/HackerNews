package me.hienngo.hackernews.domain.repo;

import java.util.List;

import me.hienngo.hackernews.modal.Item;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @author hienngo
 * @since 9/29/17
 */

public interface HackerNewsRepo {
    @GET("topstories.json")
    Observable<List<Long>> getTopStories();

    @GET("item/{id}")
    Item getItemDetail(@Path("id") long id);

}
