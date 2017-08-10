package com.ra1ph.giphysample.data.source;

import android.content.Context;

import com.google.gson.Gson;
import com.ra1ph.giphysample.data.source.local.MyObjectBox;
import com.ra1ph.giphysample.data.source.local.VotesSource;
import com.ra1ph.giphysample.data.source.local.VotesSourceImplementation;
import com.ra1ph.giphysample.data.source.remote.GiphyApi;
import com.ra1ph.giphysample.utils.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mikhail Korshunov on 10.08.17.
 */

@Module
public class GifDataSourceModule {
    @Singleton
    @Provides
    public static GifDataSource provideGifDataSource(GiphyApi giphyApi, VotesSource votesSource) {
        return new GiphyRepository(giphyApi, votesSource);
    }

    @Singleton
    @Provides
    public static GiphyApi provideGiphyApi(GsonConverterFactory gsonConverterFactory, RxJava2CallAdapterFactory rxJavaAdapterFactory) {
        return new Retrofit.Builder()
                .baseUrl(Constants.GIPHY_ENDPOINT)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaAdapterFactory)
                .build()
                .create(GiphyApi.class);
    }

    @Singleton
    @Provides
    public static VotesSource provideVotesSource(BoxStore boxStore) {
        return new VotesSourceImplementation(boxStore);
    }

    @Singleton
    @Provides
    public static BoxStore provideBoxStore(Context context) {
        return MyObjectBox.builder()
                .androidContext(context)
                .build();
    }

    @Singleton
    @Provides
    public static GsonConverterFactory provideGsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Singleton
    @Provides
    public static Gson provideGson() {
        return new Gson();
    }

    @Singleton
    @Provides
    public static RxJava2CallAdapterFactory provideRxJavaAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }
}
