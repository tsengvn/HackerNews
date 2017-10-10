package me.hienngo.hackernews.util;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author hienngo
 * @since 10/10/17
 */

public class ScrollUtil {
    public static void saveCurrentScrollPosition(@NonNull RecyclerView recyclerView, @NonNull Bundle outState) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null && layoutManager instanceof LinearLayoutManager) {
            int currentPos = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            float offsetPercent = (float)recyclerView.getChildAt(0).getTop()/(float)recyclerView.getChildAt(0).getHeight();

            outState.putInt("position", currentPos);
            outState.putFloat("offsetPercent", offsetPercent);
        }
    }

    public static void restoreLastScrollPosition(@NonNull RecyclerView recyclerView, @NonNull Bundle savedState) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null && layoutManager instanceof LinearLayoutManager
                && savedState.containsKey("position") && savedState.containsKey("offsetPercent")) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int position = savedState.getInt("position");
            float offsetPercent = savedState.getFloat("offsetPercent");
            linearLayoutManager.scrollToPosition(position);
            recyclerView.post(() -> {
                View view = linearLayoutManager.findViewByPosition(position);
                int offset = view != null ? (int) (offsetPercent * view.getHeight()) : 0;
                linearLayoutManager.scrollToPositionWithOffset(position, offset);
            });
        }
    }
}
