package com.ra1ph.giphysample.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.ra1ph.giphysample.R;
import com.ra1ph.giphysample.search.SearchGifController;
import com.ra1ph.giphysample.search.domain.GifModel;
import com.ra1ph.giphysample.watch.WatchGifController;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GiphyMainActivity extends AppCompatActivity {
    @BindView(R.id.controller_container)
    ViewGroup viewContainer;
    private Router router;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        ButterKnife.bind(this);

        router = Conductor.attachRouter(this, viewContainer, savedInstanceState);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(new SearchGifController()));
        }
    }

    @Override
    public void onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed();
        }
    }
}
