package me.hienngo.hackernews;

import android.app.Application;
import android.content.Context;

import me.hienngo.hackernews.di.component.AppComponent;
import me.hienngo.hackernews.di.component.DaggerAppComponent;
import me.hienngo.hackernews.di.module.AppModule;
import me.hienngo.hackernews.di.module.NetworkModule;

/**
 * @author hienngo
 * @since 9/29/17
 */

public class HNApp extends Application {
    private AppComponent appComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .build();
    }

    public static AppComponent appComponent(Context context) {
        return ((HNApp)context.getApplicationContext()).appComponent;
    }
}
