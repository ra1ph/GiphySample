package com.ra1ph.giphysample.search;

import com.ra1ph.giphysample.search.domain.GifModel;
import com.ra1ph.giphysample.search.domain.usecase.SearchGifUsecase;
import com.ra1ph.giphysample.search.domain.usecase.VotesCountUsecase;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Mikhail Korshunov on 10.08.17.
 */

class SearchGifPresenter implements SearchGifContract.Presenter {
    private SearchGifContract.View gifsView;
    private SearchGifUsecase searchGifsUsecase;
    private VotesCountUsecase votesCountUsecase;

    private CompositeDisposable compositeDisposable;

    SearchGifPresenter(SearchGifContract.View gifsView,
                              SearchGifUsecase searchGifsUsecase,
                              VotesCountUsecase votesCountUsecase) {
        this.gifsView = gifsView;
        this.searchGifsUsecase = searchGifsUsecase;
        this.votesCountUsecase = votesCountUsecase;
    }

    @Override
    public void seekGif(String query) {
        if (query == null || query.length() == 0) {
            gifsView.showEmptyQuery();
            return;
        }

        searchGifsUsecase.searchGifs(query)
                .subscribe(this::gifsLoaded,
                        throwable -> gifsView.showError(throwable.getLocalizedMessage()));
    }

    @Override
    public void loadMore() {
        searchGifsUsecase.loadMore()
                .subscribe(this::gifsLoaded,
                        throwable -> gifsView.showError(throwable.getLocalizedMessage()));
    }

    @Override
    public void gifSelected(GifModel gifModel) {
        gifsView.showGif(gifModel);
    }

    @Override
    public void bindView() {
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(votesCountUsecase.votesUpCount()
                .subscribe(upCount -> gifsView.showUpThumbsCount(upCount)));
        compositeDisposable.add(votesCountUsecase.votesDownCount()
                .subscribe(downCount -> gifsView.showDownThumbsCount(downCount)));

        searchGifsUsecase.restoreState()
                .subscribe(this::gifsLoaded,
                        throwable -> gifsView.showError(throwable.getLocalizedMessage()));
    }

    @Override
    public void unbindView() {
        compositeDisposable.dispose();
        compositeDisposable = null;
    }

    private void gifsLoaded(List<GifModel> gifModels) {
        if (gifModels == null || gifModels.size() == 0) {
            gifsView.showNothingFound();
        } else {
            gifsView.showGifs(gifModels);
        }
    }
}
