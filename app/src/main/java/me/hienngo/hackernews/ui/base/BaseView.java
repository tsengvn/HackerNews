package me.hienngo.hackernews.ui.base;

/**
 * @author hienngo
 * @since 9/30/17
 */

public interface BaseView {
    void showError(String message);

    void showLoading();

    void dismissLoading();
}
