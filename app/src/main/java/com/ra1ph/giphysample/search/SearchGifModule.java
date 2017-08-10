package com.ra1ph.giphysample.search;

import com.ra1ph.giphysample.data.source.GifDataSource;
import com.ra1ph.giphysample.data.source.local.VotesSource;
import com.ra1ph.giphysample.search.domain.usecase.SearchGifUsecase;
import com.ra1ph.giphysample.search.domain.usecase.VotesCountUsecase;
import com.ra1ph.giphysample.utils.NetworkUtils;
import com.ra1ph.giphysample.utils.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mikhail Korshunov on 10.08.17.
 */

@Module
public class SearchGifModule {
    private SearchGifContract.View searchGifView;

    public SearchGifModule(SearchGifContract.View searchGifView) {
        this.searchGifView = searchGifView;
    }

    @PerActivity
    @Provides
    SearchGifContract.View provideSearchGifView() {
        return searchGifView;
    }

    @PerActivity
    @Provides
    SearchGifContract.Presenter provideSearchGifPresenter(SearchGifContract.View gifsView,
                                                                 SearchGifUsecase searchGifsUsecase,
                                                                 VotesCountUsecase votesCountUsecase) {
        return new SearchGifPresenter(gifsView, searchGifsUsecase, votesCountUsecase);
    }

    @PerActivity
    @Provides
    SearchGifUsecase provideSearchGifUsecase(GifDataSource gifDataSource,
                                                    NetworkUtils networkUtils) {
        return new SearchGifUsecase(gifDataSource, networkUtils);
    }

    @PerActivity
    @Provides
    VotesCountUsecase provideVotesCountUsecase(VotesSource votesSource) {
        return new VotesCountUsecase(votesSource);
    }
}
