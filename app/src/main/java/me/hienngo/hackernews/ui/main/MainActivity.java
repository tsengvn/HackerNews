package me.hienngo.hackernews.ui.main;

import android.os.Bundle;

import me.hienngo.hackernews.R;
import me.hienngo.hackernews.ui.base.BaseActivity;

public class MainActivity extends BaseActivity<MainPresenter> {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public MainPresenter getPresenter() {
        return null;
    }

}
