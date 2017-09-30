package me.hienngo.hackernews.domain.repo;

import me.hienngo.hackernews.model.Item;

/**
 * @author hienngo
 * @since 9/30/17
 */

public interface CacheRepo  {
    void evictAll();

    void cacheItem(Item item);

    Item getCache(long id);
}
