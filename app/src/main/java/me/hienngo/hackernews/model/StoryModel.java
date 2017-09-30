package me.hienngo.hackernews.model;

import android.text.format.DateUtils;

import java.util.List;

/**
 * @author hienngo
 * @since 9/30/17
 */

public class StoryModel {
    private String title;
    private String info;
    private long itemId;
    private List<Long> kids;

    public StoryModel(Item item) {
        this.title = item.title;
        this.info = String.format("%s points by %s %s | %s comment", item.score, item.by,
                DateUtils.getRelativeTimeSpanString(item.time*1000),
                item.kids != null ? item.kids.size() : 0);
        this.itemId = item.id;
        this.kids = item.kids;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public long getItemId() {
        return itemId;
    }

    public List<Long> getKids() {
        return kids;
    }
}
