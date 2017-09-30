package me.hienngo.hackernews.di.component;

/**
 * @author hienngo
 * @since 9/29/17
 */

import javax.inject.Singleton;

import dagger.Component;
import me.hienngo.hackernews.di.module.AppModule;
import me.hienngo.hackernews.di.module.NetworkModule;
import me.hienngo.hackernews.ui.comment.CommentActivity;
import me.hienngo.hackernews.ui.main.MainActivity;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);

    void inject(CommentActivity commentActivity);
}
