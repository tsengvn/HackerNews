package me.hienngo.hackernews;

/**
 * @author hienngo
 * @since 10/2/17
 */

public class AppConfig {
    private int commentPageItem;
    private int storyPageItem;

    private AppConfig(Builder builder) {
        commentPageItem = builder.commentPageItem;
        storyPageItem = builder.storyPageItem;
    }

    public int getCommentPageItem() {
        return commentPageItem;
    }

    public int getStoryPageItem() {
        return storyPageItem;
    }

    public static final class Builder {
        private int storyPageItem;
        private int commentPageItem;

        public Builder() {
            storyPageItem = 10;
            commentPageItem = 10;
        }

        public Builder setStoryPageItem(int val) {
            storyPageItem = val;
            return this;
        }

        public Builder setCommentPageItem(int val) {
            commentPageItem = val;
            return this;
        }

        public AppConfig build() {
            return new AppConfig(this);
        }
    }
}
