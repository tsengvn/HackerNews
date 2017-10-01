package me.hienngo.hackernews.ui.base;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * @author hienngo
 * @since 10/1/17
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class BasePresenterTest {
    @Mock
    BasePresenter basePresenter;

    @Mock
    BaseView baseView;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        basePresenter = new BasePresenter<BaseView>() {
            @Override
            public void onViewReady() {

            }

            @Override
            public void onViewDestroyed() {

            }
        };
    }

    @Test
    public void testCallViewReady() {
        BasePresenter spy = spy(basePresenter);
        spy.attachView(baseView);
        verify(spy).onViewReady();
        assertEquals(baseView, spy.getView());
    }

    @Test
    public void testCallViewRemoved() {
        BasePresenter spy = spy(basePresenter);
        spy.detachView();
        verify(spy).onViewDestroyed();
        assertEquals(null, spy.getView());
    }
}