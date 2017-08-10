package com.ra1ph.giphysample;

import android.content.Context;

import com.ra1ph.giphysample.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mikhail Korshunov on 10.08.17.
 */

@Module
class AppModule {
    private GiphyApplication aplpication;

    AppModule(GiphyApplication aplpication) {
        this.aplpication = aplpication;
    }

    @Singleton
    @Provides
    GiphyApplication provideApplication() {
        return aplpication;
    }

    @Singleton
    @Provides
    Context provideApplicationContext() {
        return aplpication;
    }

    @Singleton
    @Provides
    NetworkUtils provideNetworkUtils(Context context) {
        return new NetworkUtils(context);
    }

    @Singleton
    @Provides
    Picasso providePicasso(Context context) {
        return Picasso.with(context);
    }
}
