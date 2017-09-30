package me.hienngo.hackernews.model;

import android.text.format.DateUtils;

/**
 * @author hienngo
 * @since 9/30/17
 */

public class StoryModel {
    public String title;
    public String info;

    public StoryModel(Item item) {
        this.title = item.title;
        this.info = String.format("%s points by %s %s | %s comment", item.score, item.by,
                DateUtils.getRelativeTimeSpanString(item.time*1000),
                item.kids != null ? item.kids.size() : 0);
    }
}
