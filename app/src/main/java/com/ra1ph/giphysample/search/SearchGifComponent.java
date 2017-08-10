package com.ra1ph.giphysample.search;

import com.ra1ph.giphysample.utils.PerActivity;

import dagger.Subcomponent;

/**
 * Created by Mikhail Korshunov on 10.08.17.
 */

@PerActivity
@Subcomponent(modules = { SearchGifModule.class })
public interface SearchGifComponent {
    SearchGifController inject(SearchGifController controller);
}
