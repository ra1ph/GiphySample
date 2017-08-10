package com.ra1ph.giphysample.watch;

import com.ra1ph.giphysample.utils.PerActivity;

import dagger.Subcomponent;

/**
 * Created by Mikhail Korshunov on 10.08.17.
 */

@PerActivity
@Subcomponent(modules = { WatchGifModule.class })
public interface WatchGifComponent {
    WatchGifController inject(WatchGifController controller);
}
