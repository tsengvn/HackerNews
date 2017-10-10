package me.hienngo.hackernews.domain.repo;

import com.google.gson.Gson;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import me.hienngo.hackernews.model.Item;

/**
 * @author hienngo
 * @since 9/30/17
 */

public class DiskCacheRepo implements CacheRepo {
    private static final long CACHE_SIZE = 10_000_000;
    private static final long CACHE_DURATION = TimeUnit.MINUTES.toMillis(5);
    private DiskLruCache lruCache;
    private final Gson gson;
    private final File cacheDir;

    public DiskCacheRepo(File cacheDir, Gson gson) {
        this.gson = gson;
        this.cacheDir = cacheDir;
        openCache();
    }

    private void openCache() {
        try {
            File itemFolder = new File(cacheDir, "item");
            itemFolder.mkdir();
            lruCache = DiskLruCache.open(itemFolder, 2, 2, CACHE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cacheItem(Item item) {
        if (lruCache != null) {
            try {
                DiskLruCache.Editor editor = lruCache.edit(String.valueOf(item.id));
                editor.set(0, gson.toJson(item));
                editor.set(1, String.valueOf(System.currentTimeMillis()));
                editor.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Item getCache(long id) {
        if (lruCache != null) {
            try {
                DiskLruCache.Snapshot snapshot = lruCache.get(String.valueOf(id));
                long savedTime = Long.parseLong(snapshot.getString(1));
                if (System.currentTimeMillis() - savedTime < CACHE_DURATION) {
                    return gson.fromJson(snapshot.getString(0), Item.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void evictAll() {
        if (lruCache != null) {
            try {
                lruCache.delete();
                openCache();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
