package me.hienngo.hackernews.ui.main;

import android.content.Intent;
import android.os.Build;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.util.ArrayList;
import java.util.List;

import me.hienngo.hackernews.BuildConfig;
import me.hienngo.hackernews.model.Item;
import me.hienngo.hackernews.model.StoryModel;
import me.hienngo.hackernews.ui.comment.CommentActivity;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.robolectric.Robolectric.getShadowsAdapter;

/**
 * @author hienngo
 * @since 10/1/17
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    private ActivityController<MainActivity> activityController;

    @Mock
    MainPresenter mainPresenter;
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        activityController = ActivityController.of(getShadowsAdapter(), new MainActivity(){
            @Override
            public MainPresenter createPresenter() {
                return mainPresenter;
            }
        }, null);
    }

    @Test
    public void testViewLifeCycle() {
        MainActivity mainActivity = activityController.create().start().stop().destroy().get();
        Assert.assertNotNull(mainActivity);
        verify(mainPresenter).attachView(any(MainView.class));
        verify(mainPresenter).detachView();
    }

    @Test
    public void testReceiveData() {
        MainActivity mainActivity = activityController.create().get();
        Assert.assertNotNull(mainActivity);
        verify(mainPresenter).attachView(any(MainView.class));

        List<StoryModel> modelList = new ArrayList<>();
        modelList.add(new StoryModel(new Item()));
        modelList.add(new StoryModel(new Item()));

        mainActivity.onReceivedData(modelList, false);
        assertEquals(2, mainActivity.recyclerView.getAdapter().getItemCount());

        mainActivity.onReceivedData(modelList, true);
        assertEquals(4, mainActivity.recyclerView.getAdapter().getItemCount());

        mainActivity.onReceivedData(new ArrayList<>(), true);
        assertEquals(4, mainActivity.recyclerView.getAdapter().getItemCount());

        mainActivity.onReceivedData(modelList, false);
        assertEquals(2, mainActivity.recyclerView.getAdapter().getItemCount());
    }

    @Test
    public void testOpenCommentScreenWhenClickedOnItem() {
        MainActivity mainActivity = activityController.create().start().resume().visible().get();
        Assert.assertNotNull(mainActivity);

        List<StoryModel> modelList = new ArrayList<>();
        Item item = new Item();
        item.id = 1;
        item.title = "test title";

        modelList.add(new StoryModel(item));

        mainActivity.onReceivedData(modelList, false);
        assertEquals(1, mainActivity.recyclerView.getAdapter().getItemCount());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mainActivity.recyclerView.findViewHolderForAdapterPosition(0).itemView.performClick();

        Intent intent = Shadows.shadowOf(mainActivity).peekNextStartedActivity();
        assertEquals("test title", intent.getExtras().getString("title"));
        assertEquals(1, intent.getExtras().getLong("itemId", 0));
        assertEquals(CommentActivity.class.getCanonicalName(), intent.getComponent().getClassName());
    }
}