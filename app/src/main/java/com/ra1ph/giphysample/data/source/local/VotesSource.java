package com.ra1ph.giphysample.data.source.local;

import com.ra1ph.giphysample.search.domain.GifModel;

import io.reactivex.Observable;

/**
 * Created by Mikhail Korshunov on 09.08.17.
 */

public interface VotesSource {
    void vote(String gilphyGifId, GifModel.Vote vote);
    Observable<Long> votesUpCount();
    Observable<Long> votesDownCount();
    GifModel.Vote voteForGilphyGif(String gilphyGifId);
}
