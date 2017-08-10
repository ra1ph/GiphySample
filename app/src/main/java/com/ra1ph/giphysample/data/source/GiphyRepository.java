package com.ra1ph.giphysample.data.source;

import android.support.annotation.Nullable;

import com.ra1ph.giphysample.data.source.local.VotesSource;
import com.ra1ph.giphysample.data.source.remote.GiphyApi;
import com.ra1ph.giphysample.data.source.remote.GiphyGifEntity;
import com.ra1ph.giphysample.data.source.remote.GiphyPagination;
import com.ra1ph.giphysample.search.domain.GifModel;
import com.ra1ph.giphysample.search.domain.GifsList;
import com.ra1ph.giphysample.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Mikhail Korshunov on 09.08.17.
 */

class GiphyRepository implements GifDataSource {
    private static final int GIFS_LIMIT = 30;

    private GiphyApi giphyApi;
    private VotesSource votesSource;

    GiphyRepository(GiphyApi giphyApi, VotesSource votesSource) {
        this.giphyApi = giphyApi;
        this.votesSource = votesSource;
    }

    @Override
    public Observable<GifsList> loadGifs(@Nullable String query, int offset) {
        if (query == null || query.length() == 0) {
            return Observable.just(new GifsList());
        }

        return giphyApi.searchGifs(Constants.GIPHY_APP_KEY, query, GIFS_LIMIT, offset)
                .map(response -> {
                    if (!response.getMeta().isOk() || response.getData() == null) {
                        throw new RuntimeException(response.getMeta().toString());
                    }

                    ArrayList<GifModel> newItems = new ArrayList<>();
                    for (GiphyGifEntity giphyEntity : response.getData()) {
                        newItems.add(giphyEntity.toGifModel(
                                votesSource.voteForGilphyGif(giphyEntity.getGiphyId())));
                    }

                    //pagination can't be null because of meta is OK
                    return new GifsList(newItems, response.getPagination().getOffset(), response.getPagination().getTotalCount());
                });
    }

    @Override
    public Observable<GifModel> saveGif(final GifModel gif) {
        return Observable.create(e -> {
            votesSource.vote(gif.getGilfyGifId(), gif.getVote());
            e.onNext(gif);
            e.onComplete();
        });
    }
}
