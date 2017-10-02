package me.hienngo.hackernews.domain.interactor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import me.hienngo.hackernews.AppConfig;
import me.hienngo.hackernews.domain.repo.CacheRepo;
import me.hienngo.hackernews.domain.repo.HackerNewsRepo;
import me.hienngo.hackernews.model.Item;
import me.hienngo.hackernews.ui.BaseTest;
import retrofit2.mock.Calls;
import rx.Observable;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author hienngo
 * @since 10/2/17
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class GetTopStoriesTest extends BaseTest{
    private GetTopStories getTopStories;

    @Mock
    HackerNewsRepo hackerNewsRepo;

    @Mock
    CacheRepo cacheRepo;

    @Before
    public void setup() {
        super.setup();
        Item item1 = new Item();
        item1.id = 1;
        item1.text = "test 1";
        item1.title = "title 1";
        item1.by = "Hien Ngo";
        item1.score = 1;

        Item item2 = new Item();
        item2.id = 2;
        item2.text = "test 2";
        item2.title = "title 2";
        item2.by = "Someone";
        item2.score = 5;

        Item item3 = new Item();
        item3.id = 3;
        item3.text = "test 3";
        item3.title = "title 3";
        item3.by = "July";
        item3.score = 15;

        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        ids.add(3L);

        AppConfig appConfig = new AppConfig.Builder().setStoryPageItem(2).build();
        getTopStories = new GetTopStories(hackerNewsRepo, cacheRepo, appConfig);

        when(hackerNewsRepo.getTopStories()).thenReturn(Observable.just(ids));
        when(hackerNewsRepo.getItem(1L)).thenReturn(Calls.response(item1));
        when(hackerNewsRepo.getItem(2L)).thenReturn(Calls.response(item2));
        when(hackerNewsRepo.getItem(3L)).thenReturn(Calls.response(item3));
    }

    @Test
    public void testGetTopDataWithNetwork() {

        when(cacheRepo.getCache(anyLong())).thenReturn(null);

        getTopStories.getTopStories(false)
                .subscribe(storyModels -> {
                    Assert.assertEquals(2, storyModels.size());
                    Assert.assertEquals(1, storyModels.get(0).getItemId());
                    Assert.assertEquals(2, storyModels.get(1).getItemId());
                });
        Robolectric.flushBackgroundThreadScheduler();
        verify(hackerNewsRepo).getTopStories();

    }

    @Test
    public void testGetTopDataWithCache() {
        Item cahedItem = new Item();
        cahedItem.id = 2;
        cahedItem.title = "cached";
        when(cacheRepo.getCache(1L)).thenReturn(null);
        when(cacheRepo.getCache(2L)).thenReturn(cahedItem);
        getTopStories.getTopStories(false)
                .subscribe(storyModels -> {
                    Assert.assertEquals(2, storyModels.size());
                    Assert.assertEquals(1, storyModels.get(0).getItemId());
                    Assert.assertEquals("title 1", storyModels.get(0).getTitle());
                    Assert.assertEquals(2, storyModels.get(1).getItemId());
                    Assert.assertEquals("cached", storyModels.get(1).getTitle());
                });
        Robolectric.flushBackgroundThreadScheduler();
        verify(hackerNewsRepo).getTopStories();
    }

    @Test
    public void testReturnCurrentTopDataWhenScreenRestarted() {
        testGetTopDataWithNetwork();

        getTopStories.getTopStories(false)
                .subscribe(storyModels -> {
                    Assert.assertEquals(2, storyModels.size());
                    Assert.assertEquals(1, storyModels.get(0).getItemId());
                    Assert.assertEquals(2, storyModels.get(1).getItemId());
                });
        verify(hackerNewsRepo, timeout(0)).getTopStories();
    }

    @Test
    public void testLoadMore() {
        testGetTopDataWithNetwork();

        getTopStories.loadNext()
                .subscribe(storyModels -> {
                    Assert.assertEquals(1, storyModels.size());
                    Assert.assertEquals(3, storyModels.get(0).getItemId());
                    Assert.assertEquals("title 3", storyModels.get(0).getTitle());
                });
    }

    @Test
    public void testRefreshData() {
        testGetTopDataWithNetwork();
        GetTopStories spy = spy(getTopStories);

        spy.getTopStories(true)
                .subscribe(storyModels -> {
                    Assert.assertEquals(2, storyModels.size());
                    Assert.assertEquals(1, storyModels.get(0).getItemId());
                    Assert.assertEquals(2, storyModels.get(1).getItemId());
                });

        verify(spy).clearCurrentData();
        verify(cacheRepo).evictAll();
    }
}