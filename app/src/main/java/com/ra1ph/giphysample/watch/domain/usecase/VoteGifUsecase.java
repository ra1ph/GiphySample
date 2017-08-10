package com.ra1ph.giphysample.watch.domain.usecase;

import com.ra1ph.giphysample.data.source.GifDataSource;
import com.ra1ph.giphysample.search.domain.GifModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Mikhail Korshunov on 10.08.17.
 */

public class VoteGifUsecase {
    private GifDataSource gifDataSource;

    public VoteGifUsecase(GifDataSource gifDataSource) {
        this.gifDataSource = gifDataSource;
    }

    public Observable<GifModel> saveVote(GifModel gifModel, GifModel.Vote vote) {
        gifModel.setVote(vote);
        return Observable.create(subscriber -> gifDataSource.saveGif(gifModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(gifModel1 -> {
                    subscriber.onNext(gifModel);
                    subscriber.onComplete();
                }, subscriber::onError));
    }
}
