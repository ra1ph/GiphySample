package com.ra1ph.giphysample;

import android.app.Application;
import android.content.Context;

/**
 * Created by Mikhail Korshunov on 09.08.17.
 */

public class GiphyApplication extends Application {
    private AppComponent appComponent;

    public static GiphyApplication get(Context context) {
        return (GiphyApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initAppComponent();
    }

    private void initAppComponent() {
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
