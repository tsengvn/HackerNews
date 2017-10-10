package me.hienngo.hackernews.util;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * @author hienngo
 * @since 10/10/17
 */

public class ScrollUtil {
    public static void saveCurrentScrollPosition(@NonNull RecyclerView recyclerView, @NonNull Bundle outState) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null && layoutManager instanceof LinearLayoutManager) {
            int currentPos = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            int offset = recyclerView.getChildAt(0).getTop();

            outState.putInt("position", currentPos);
            outState.putInt("offset", offset);
        }
    }

    public static void restoreLastScrollPosition(@NonNull RecyclerView recyclerView, @NonNull Bundle savedState) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null && layoutManager instanceof LinearLayoutManager
                && savedState.containsKey("position") && savedState.containsKey("offset")) {
            ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(savedState.getInt("position"),
                    savedState.getInt("offset"));
        }
    }
}
