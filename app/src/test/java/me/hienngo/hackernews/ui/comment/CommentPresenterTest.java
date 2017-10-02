package me.hienngo.hackernews.ui.comment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import me.hienngo.hackernews.domain.interactor.GetStoryComments;
import me.hienngo.hackernews.model.CommentModel;
import me.hienngo.hackernews.ui.BaseTest;
import rx.Observable;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author hienngo
 * @since 10/1/17
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class CommentPresenterTest extends BaseTest {
    private CommentPresenter commentPresenter;

    @Mock
    CommentView commentView;

    @Mock
    GetStoryComments getStoryComments;

    @Before
    public void setup() {
        super.setup();
        commentPresenter = new CommentPresenter(getStoryComments, 0);
    }

    @Test
    public void testLoadDataWhenViewReady() {
        ArgumentCaptor<List<CommentModel>> dataCaptor = ArgumentCaptor.forClass(ArrayList.class);
        ArgumentCaptor<Boolean> loadMoreCaptor = ArgumentCaptor.forClass(Boolean.class);

        List<CommentModel> data = new ArrayList<>();

        when(getStoryComments.loadCommentForStory(0)).thenReturn(Observable.just(data));

        commentPresenter.attachView(commentView);

        verify(commentView).showLoading();
        verify(commentView).onReceiveCommentData(dataCaptor.capture(), loadMoreCaptor.capture());
        verify(commentView).dismissLoading();
        Assert.assertEquals(data, dataCaptor.getValue());
        Assert.assertFalse(loadMoreCaptor.getValue());
    }

    @Test
    public void testLoadDataErrorWhenViewReady() {
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

        when(getStoryComments.loadCommentForStory(0)).thenReturn(Observable.error(new RuntimeException("error message")));

        commentPresenter.attachView(commentView);
        verify(commentView).showLoading();
        verify(commentView).dismissLoading();
        verify(commentView).showError(stringCaptor.capture());
        Assert.assertEquals("error message", stringCaptor.getValue());
    }

    @Test
    public void testViewDestroyed() {
        testLoadDataWhenViewReady();
        commentPresenter.detachView();
        Assert.assertNull(commentPresenter.subscription);

    }

    @Test
    public void testLoadMore() {
        testLoadDataWhenViewReady();

        ArgumentCaptor<List<CommentModel>> dataCaptor = ArgumentCaptor.forClass(ArrayList.class);
        ArgumentCaptor<Boolean> loadMoreCaptor = ArgumentCaptor.forClass(Boolean.class);
        List<CommentModel> dataLoadMore = new ArrayList<>();
        when(getStoryComments.loadNext()).thenReturn(Observable.just(dataLoadMore));


        commentPresenter.loadMore();

        Assert.assertTrue(commentPresenter.subscription == null || commentPresenter.subscription.isUnsubscribed());

        verify(commentView, times(2)).showLoading();
        verify(commentView, times(2)).onReceiveCommentData(dataCaptor.capture(), loadMoreCaptor.capture());
        verify(commentView, times(2)).dismissLoading();
        Assert.assertEquals(dataLoadMore, dataCaptor.getValue());
        Assert.assertTrue(loadMoreCaptor.getValue());
    }
}