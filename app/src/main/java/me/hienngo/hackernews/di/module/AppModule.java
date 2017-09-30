package me.hienngo.hackernews.di.module;

import android.content.Context;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.hienngo.hackernews.domain.interactor.GetStoryComments;
import me.hienngo.hackernews.domain.interactor.GetTopStories;
import me.hienngo.hackernews.domain.repo.CacheRepo;
import me.hienngo.hackernews.domain.repo.DiskCacheRepo;
import me.hienngo.hackernews.domain.repo.HackerNewsRepo;

/**
 * @author hienngo
 * @since 9/29/17
 */
@Module
public class AppModule {
    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Singleton @Provides
    public Context provideContext() {
        return this.context;
    }

    @Singleton @Provides
    public Gson provideGson() {
        return new Gson();
    }

    @Singleton @Provides
    public GetTopStories provideGetTopStories(HackerNewsRepo hackerNewsRepo, CacheRepo cacheRepo) {
        return new GetTopStories(hackerNewsRepo, cacheRepo);
    }

    @Singleton @Provides
    public GetStoryComments provideGetStoryComments(HackerNewsRepo hackerNewsRepo, CacheRepo cacheRepo) {
        return new GetStoryComments(hackerNewsRepo, cacheRepo);
    }

    @Singleton @Provides
    public CacheRepo provideCacheRepo(Gson gson, Context context) {
        return new DiskCacheRepo(context.getCacheDir(), gson);
    }
}
