package me.hienngo.hackernews.domain.interactor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;

import me.hienngo.hackernews.AppConfig;
import me.hienngo.hackernews.domain.repo.CacheRepo;
import me.hienngo.hackernews.domain.repo.HackerNewsRepo;
import me.hienngo.hackernews.model.Item;
import me.hienngo.hackernews.ui.BaseTest;
import retrofit2.mock.Calls;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author hienngo
 * @since 10/2/17
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class GetStoryCommentsTest extends BaseTest {
    @Mock
    HackerNewsRepo hackerNewsRepo;

    @Mock
    CacheRepo cacheRepo;

    private GetStoryComments getStoryComments;

    @Before
    public void setup() {
        super.setup();

        AppConfig appConfig = new AppConfig.Builder().setCommentPageItem(2).build();

        Item item1 = new Item();
        item1.id = 1;
        item1.text = "comment 1";
        item1.kids = new ArrayList<>(Arrays.asList(2L));

        Item item2 = new Item();
        item2.id = 2;
        item2.text = "comment 2";

        Item item3 = new Item();
        item3.id = 3;
        item3.text = "comment 3";

        Item story = new Item();
        story.id = 4;
        story.kids = new ArrayList<>(Arrays.asList(1L, 3L));

        when(hackerNewsRepo.getItem(1L)).thenReturn(Calls.response(item1));
        when(hackerNewsRepo.getItem(2L)).thenReturn(Calls.response(item2));
        when(hackerNewsRepo.getItem(3L)).thenReturn(Calls.response(item3));
        when(hackerNewsRepo.getItem(4L)).thenReturn(Calls.response(story));

        getStoryComments = new GetStoryComments(hackerNewsRepo, cacheRepo, appConfig);
    }

    @Test
    public void testLoadComments() {
        getStoryComments.loadCommentForStory(4L)
                .subscribe(commentModels -> {
                    assertEquals(2, commentModels.size());
                    assertEquals(1, commentModels.get(0).getItemId());
                    assertEquals(2, commentModels.get(1).getItemId());
                });
    }

    @Test
    public void testLoadNextComments() {
        testLoadComments();
        getStoryComments.loadNext()
                .subscribe(commentModels -> {
                    assertEquals(1, commentModels.size());
                    assertEquals(3, commentModels.get(0).getItemId());
                });
    }

    @Test
    public void testThrowErrorWhenNoCommentForStory() {
        when(hackerNewsRepo.getItem(5L)).thenReturn(Calls.response(new Item()));
        getStoryComments.loadCommentForStory(5L)
                .subscribe(commentModels -> {}, throwable -> {
                    //or we create a new exception class for empty case
                    assertEquals("No comment", throwable.getMessage());
                    assertTrue(throwable instanceof RuntimeException);
                });


    }

    @Test
    public void testThrowErrorCanNotRetrieveStory() {
        when(hackerNewsRepo.getItem(6L)).thenReturn(Calls.failure(null));
        getStoryComments.loadCommentForStory(6L)
                .subscribe(commentModels -> {}, throwable -> {
                    assertEquals("Can not retrieved story's comment", throwable.getMessage());
                    assertTrue(throwable instanceof RuntimeException);
                });
    }
}