package me.hienngo.hackernews.ui.main;

import android.os.Build;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import me.hienngo.hackernews.BuildConfig;

/**
 * @author hienngo
 * @since 10/1/17
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    private MainActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(MainActivity.class).create().get();
    }

    @Test
    public void testNotNull() {
        Assert.assertNotNull(activity);
        Assert.assertEquals("HackerNews" ,activity.getTitle());
    }
}