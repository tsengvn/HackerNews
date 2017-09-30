package me.hienngo.hackernews.ui.comment;

import java.util.List;

import me.hienngo.hackernews.model.CommentModel;
import me.hienngo.hackernews.ui.base.BaseView;

/**
 * @author hienngo
 * @since 9/30/17
 */

interface CommentView extends BaseView {
    void onReceiveCommentData(List<CommentModel> commentModelList, boolean loadMore);
}
