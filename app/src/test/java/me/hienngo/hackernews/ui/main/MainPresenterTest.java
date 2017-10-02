package me.hienngo.hackernews.ui.main;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import me.hienngo.hackernews.domain.interactor.GetTopStories;
import me.hienngo.hackernews.model.StoryModel;
import me.hienngo.hackernews.ui.BaseTest;
import rx.Observable;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author hienngo
 * @since 10/1/17
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class MainPresenterTest extends BaseTest{
    MainPresenter mainPresenter;

    @Mock
    MainView mainView;

    @Mock
    GetTopStories getTopStories;

    @Before
    public void setup() {
        super.setup();
        mainPresenter = new MainPresenter(getTopStories);
    }

    @Test
    public void testLoadDataWhenViewReady() {
        List<StoryModel> storyModels = new ArrayList<>();
        Observable<List<StoryModel>> listObservable = Observable.just(storyModels);
        ArgumentCaptor<Boolean> captorBoolean = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<List> captorList = ArgumentCaptor.forClass(List.class);

        when(getTopStories.getTopStories(captorBoolean.capture())).thenReturn(listObservable);

        mainPresenter.attachView(mainView);
        verify(mainView).showLoading();
        verify(mainView).onReceivedData(captorList.capture(), captorBoolean.capture());
        verify(mainView).dismissLoading();
        assertEquals(Boolean.FALSE, captorBoolean.getAllValues().get(0));
        assertEquals(Boolean.FALSE, captorBoolean.getAllValues().get(1));
        assertEquals(storyModels, captorList.getValue());
    }

    @Test
    public void testLoadDataErrorWhenViewReady() {
        Exception exception = new Exception("Test error");
        Observable<List<StoryModel>> errorObservable = Observable.error(exception);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(getTopStories.getTopStories(anyBoolean())).thenReturn(errorObservable);

        mainPresenter.attachView(mainView);
        verify(mainView).showLoading();
        verify(mainView, never()).onReceivedData(anyList(), anyBoolean());
        verify(mainView).dismissLoading();
        verify(mainView).showError(captor.capture());
        assertEquals("Test error", captor.getValue());

    }
    @Test
    public void testRefreshData() {
        testLoadDataWhenViewReady();

        ArgumentCaptor<Boolean> captorBoolean = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<List> captorList = ArgumentCaptor.forClass(List.class);

        //refresh
        List<StoryModel> storyModels2 = new ArrayList<>();
        Observable<List<StoryModel>> listObservable2 = Observable.just(storyModels2);
        when(getTopStories.getTopStories(true)).thenReturn(listObservable2);

        mainPresenter.refresh();
        verify(mainView, times(2)).showLoading();
        verify(mainView, times(2)).onReceivedData(captorList.capture(), captorBoolean.capture());
        verify(mainView, times(2)).dismissLoading();
        assertEquals(Boolean.FALSE, captorBoolean.getValue());
        assertEquals(storyModels2, captorList.getValue());
    }

    @Test
    public void testLoadMore() {
        testLoadDataWhenViewReady();

        ArgumentCaptor<Boolean> captorBoolean = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<List> captorList = ArgumentCaptor.forClass(List.class);

        //load more
        List<StoryModel> list = new ArrayList<>();
        when(getTopStories.loadNext()).thenReturn(Observable.just(list));

        mainPresenter.loadMore();
        verify(mainView, times(2)).showLoading();
        verify(mainView, times(2)).onReceivedData(captorList.capture(), captorBoolean.capture());
        verify(mainView, times(2)).dismissLoading();
        assertEquals(Boolean.TRUE, captorBoolean.getValue());
        assertEquals(list, captorList.getValue());

    }


    @Test
    public void testViewDestroyed() {
        testLoadDataWhenViewReady();

        mainPresenter.detachView();
        assertNull(mainPresenter.subscription);
    }
}