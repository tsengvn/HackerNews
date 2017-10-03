package me.hienngo.hackernews.domain.repo;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;

import me.hienngo.hackernews.model.Item;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author hienngo
 * @since 10/2/17
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class DiskCacheRepoTest {
    CacheRepo cacheRepo;
    @Before
    public void setup() {
        Gson gson = new Gson();

        cacheRepo = new DiskCacheRepo(RuntimeEnvironment.application.getCacheDir(), gson);
    }

    @Test
    public void testCacheItem() {
        Item item = new Item();
        item.id = 1;
        item.text = "test";
        item.kids = new ArrayList<>(Arrays.asList(1L,2L));
        item.by = "Hien Ngo";
        item.score = 40;
        item.title = "title";

        cacheRepo.cacheItem(item);
        Item cacheItem = cacheRepo.getCache(1);
        assertEquals(item.id, cacheItem.id);
        assertEquals(item.text, cacheItem.text);
        assertEquals(item.kids, cacheItem.kids);
        assertEquals(item.by, cacheItem.by);
        assertEquals(item.score, cacheItem.score);
        assertEquals(item.title, cacheItem.title);

        cacheRepo.evictAll();
        cacheItem = cacheRepo.getCache(1);
        assertNull(cacheItem);
    }
}