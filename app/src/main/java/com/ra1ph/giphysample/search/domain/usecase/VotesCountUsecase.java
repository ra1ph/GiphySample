package com.ra1ph.giphysample.search.domain.usecase;

import com.ra1ph.giphysample.data.source.local.VotesSource;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Mikhail Korshunov on 10.08.17.
 */

public class VotesCountUsecase {
    private VotesSource votesSource;

    public VotesCountUsecase(VotesSource votesSource) {
        this.votesSource = votesSource;
    }

    public Observable<Long> votesUpCount() {
        return Observable.create(subscriber -> votesSource.votesUpCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber::onNext));
    }

    public Observable<Long> votesDownCount() {
        return Observable.create(subscriber -> votesSource.votesDownCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber::onNext));
    }
}
