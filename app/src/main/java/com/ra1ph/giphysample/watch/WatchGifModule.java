package com.ra1ph.giphysample.watch;

import com.ra1ph.giphysample.data.source.GifDataSource;
import com.ra1ph.giphysample.utils.PerActivity;
import com.ra1ph.giphysample.watch.domain.usecase.VoteGifUsecase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mikhail Korshunov on 10.08.17.
 */

@Module
public class WatchGifModule {
    private WatchGifContract.View watchGifView;

    public WatchGifModule(WatchGifContract.View watchGifView) {
        this.watchGifView = watchGifView;
    }

    @PerActivity
    @Provides
    WatchGifContract.View provideSearchGifView() {
        return watchGifView;
    }

    @PerActivity
    @Provides
    WatchGifContract.Presenter provideWatchGifPresenter(WatchGifContract.View gifsView,
                                                               VoteGifUsecase voteGifUsecase) {
        return new WatchGifPresenter(gifsView, voteGifUsecase);
    }

    @PerActivity
    @Provides
    VoteGifUsecase provideVoteGifUsecase(GifDataSource gifDataSource) {
        return new VoteGifUsecase(gifDataSource);
    }
}
