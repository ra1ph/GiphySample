package com.ra1ph.giphysample.data.source;

import android.support.annotation.Nullable;

import com.ra1ph.giphysample.search.domain.GifModel;
import com.ra1ph.giphysample.search.domain.GifsList;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Mikhail Korshunov on 09.08.17.
 */

public interface GifDataSource {
    Observable<GifsList> loadGifs(@Nullable String query, int offset);
    Observable<GifModel> saveGif(GifModel gif);
}
