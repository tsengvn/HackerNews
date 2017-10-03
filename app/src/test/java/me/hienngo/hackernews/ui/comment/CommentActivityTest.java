package me.hienngo.hackernews.ui.comment;

import android.content.Intent;
import android.os.Build;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.util.ArrayList;
import java.util.List;

import me.hienngo.hackernews.BuildConfig;
import me.hienngo.hackernews.model.CommentModel;
import me.hienngo.hackernews.model.Item;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.robolectric.Robolectric.getShadowsAdapter;

/**
 * @author hienngo
 * @since 10/3/17
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class CommentActivityTest {
    private ActivityController<CommentActivity> activityController;

    @Mock
    CommentPresenter commentPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Intent intent = new Intent();
        intent.putExtra("itemId", 1);
        intent.putExtra("title", "test title");
        activityController = ActivityController.of(getShadowsAdapter(), new CommentActivity(){
            @Override
            public CommentPresenter createPresenter() {
                return commentPresenter;
            }
        }, intent);
    }

    @Test
    public void testViewLifeCycle() {
        CommentActivity commentActivity = activityController.create().start().stop().destroy().get();
        Assert.assertNotNull(commentActivity);
        verify(commentPresenter).attachView(any(CommentView.class));
        verify(commentPresenter).detachView();
        assertEquals("test title", commentActivity.getSupportActionBar().getTitle());
    }

    @Test
    public void testReceiveData() {
        CommentActivity commentActivity = activityController.create().visible().get();
        Assert.assertNotNull(commentActivity);
        verify(commentPresenter).attachView(any(CommentView.class));

        List<CommentModel> commentModelList = new ArrayList<>();
        commentModelList.add(new CommentModel(new Item(), 0));
        commentModelList.add(new CommentModel(new Item(), 0));
        commentModelList.add(new CommentModel(new Item(), 0));

        commentActivity.onReceiveCommentData(commentModelList, false);
        assertEquals(3, commentActivity.recyclerView.getAdapter().getItemCount());

        commentActivity.onReceiveCommentData(commentModelList, true);
        assertEquals(6, commentActivity.recyclerView.getAdapter().getItemCount());


        commentActivity.onReceiveCommentData(commentModelList, false);
        assertEquals(3, commentActivity.recyclerView.getAdapter().getItemCount());

    }
}