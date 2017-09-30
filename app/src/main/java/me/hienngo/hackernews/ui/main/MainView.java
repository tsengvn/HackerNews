package me.hienngo.hackernews.ui.main;

import java.util.List;

import me.hienngo.hackernews.model.StoryModel;
import me.hienngo.hackernews.ui.base.BaseView;

/**
 * @author hienngo
 * @since 9/30/17
 */

public interface MainView extends BaseView {
    void onReceivedData(List<StoryModel> itemList, boolean more);
}
