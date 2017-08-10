package com.ra1ph.giphysample.search.domain.usecase;

import android.accounts.NetworkErrorException;
import android.support.annotation.Nullable;

import com.ra1ph.giphysample.data.source.GifDataSource;
import com.ra1ph.giphysample.search.domain.GifModel;
import com.ra1ph.giphysample.utils.NetworkUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Mikhail Korshunov on 10.08.17.
 */

public class SearchGifUsecase {
    private GifDataSource dataSource;
    private NetworkUtils networkUtils;

    @Nullable
    private SearchGifUsecaseState currentState;
    private Disposable lastRequest;

    public SearchGifUsecase(GifDataSource dataSource, NetworkUtils networkUtils) {
        this.dataSource = dataSource;
        this.networkUtils = networkUtils;
    }

    public Observable<List<GifModel>> searchGifs(String query) {
        currentState = new SearchGifUsecaseState(query);
        return createLoadGifsObservable(currentState, query, 0);
    }

    public Observable<List<GifModel>> loadMore() {
        if (currentState == null) {
            return Observable.error(new NullPointerException("There is not any last queries"));
        }

        if (!currentState.hasMore) {
            return Observable.error(new IndexOutOfBoundsException("There is no more items to download"));
        }

        return createLoadGifsObservable(currentState, currentState.currentQuery, currentState.offset);
    }

    private Observable<List<GifModel>> createLoadGifsObservable(SearchGifUsecaseState currentState, String query, int offset) {
        if (lastRequest != null && !lastRequest.isDisposed()) {
            lastRequest.dispose();
        }

        return Observable.create(subscriber -> {
            if (!networkUtils.isConnected()) {
                subscriber.onError(new NetworkErrorException("There is no internet connection"));
                return;
            }

            lastRequest = dataSource.loadGifs(query, offset)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(gifsList -> {
                        currentState.addModels(gifsList.getGifModels(), gifsList.getNextOffset(), gifsList.hasMore());
                        subscriber.onNext(currentState.currentModels);
                        subscriber.onComplete();
                    }, subscriber::onError);
        });
    }

    public Observable<List<GifModel>> restoreState() {
        return Observable.just(currentState == null ? Collections.emptyList() : currentState.currentModels);
    }

    private static class SearchGifUsecaseState implements Serializable{
        private String currentQuery;
        private List<GifModel> currentModels;
        private int offset;
        private boolean hasMore;

        private SearchGifUsecaseState(String currentQuery) {
            this.currentQuery = currentQuery;
            currentModels = new ArrayList<>();
            offset = 0;
            hasMore = true;
        }

        private void addModels(List<GifModel> models, int nextOffset, boolean hasMore) {
            currentModels.addAll(models);
            offset = nextOffset;
            this.hasMore = hasMore;
        }
    }
}
