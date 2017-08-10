package com.ra1ph.giphysample;

import com.ra1ph.giphysample.data.source.GifDataSourceModule;
import com.ra1ph.giphysample.search.SearchGifComponent;
import com.ra1ph.giphysample.search.SearchGifModule;
import com.ra1ph.giphysample.watch.WatchGifComponent;
import com.ra1ph.giphysample.watch.WatchGifModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Mikhail Korshunov on 10.08.17.
 */

@Singleton
@Component(modules = { GifDataSourceModule.class, AppModule.class })
public interface AppComponent {
    SearchGifComponent searchGifComponent(SearchGifModule searchGifModule);
    WatchGifComponent watchGifComponent(WatchGifModule watchGifModule);
}
