package me.hienngo.hackernews.domain.repo;

import java.util.List;

import me.hienngo.hackernews.model.Item;
import retrofit2.Call;
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

//    @GET("item/{id}.json")
//    Call<Item> getItemDetail(@Path("id") long id);
//
//    @GET("item/{id}.json")
//    Observable<Item> getItemDetail(@Path("id") long id);

    @GET("item/{id}.json")
    Call<Item> getItem(@Path("id") long id);

}
