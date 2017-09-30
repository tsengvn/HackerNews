package me.hienngo.hackernews.model;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.TypedValue;

/**
 * @author hienngo
 * @since 9/30/17
 */

public class CommentModel {
    private long itemId;
    private int level;
    private Spanned comment;
    private String info;

    public CommentModel(Item item, int level) {
        this.itemId = item.id;
        this.level = level;
        if (!TextUtils.isEmpty(item.text)) {
            this.comment = Html.fromHtml(item.text);
        }
        this.info = String.format("%s %s", item.by,
                DateUtils.getRelativeTimeSpanString(item.time*1000));
    }

    public long getItemId() {
        return itemId;
    }

    public int getLevel() {
        return level;
    }

    public Spanned getComment() {
        return comment;
    }

    public String getInfo() {
        return info;
    }

    public int getPadding(Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, level*24, context.getResources().getDisplayMetrics());
    }
}
