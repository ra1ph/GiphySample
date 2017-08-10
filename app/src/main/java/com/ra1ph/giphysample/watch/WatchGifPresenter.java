package com.ra1ph.giphysample.watch;

import android.support.annotation.Nullable;

import com.ra1ph.giphysample.search.domain.GifModel;
import com.ra1ph.giphysample.watch.domain.usecase.VoteGifUsecase;

/**
 * Created by Mikhail Korshunov on 10.08.17.
 */

class WatchGifPresenter implements WatchGifContract.Presenter {
    private WatchGifContract.View watchGifView;
    private VoteGifUsecase voteGifUsecase;

    @Nullable
    private GifModel gifModel;

    WatchGifPresenter(WatchGifContract.View watchGifView, VoteGifUsecase voteGifUsecase) {
        this.watchGifView = watchGifView;
        this.voteGifUsecase = voteGifUsecase;
    }

    @Override
    public void setModel(@Nullable GifModel gifModel) {
        this.gifModel = gifModel;
        if (gifModel == null) {
            return;
        }

        watchGifView.playMp4Video(gifModel.getMp4Url());
        watchGifView.showVote(gifModel.getVote());
    }

    @Override
    public void thumbUp() {
        if (gifModel == null) {
            return;
        }
        saveVote(gifModel.getVote().equals(GifModel.Vote.UP) ? GifModel.Vote.NONE : GifModel.Vote.UP);
    }

    @Override
    public void thumbDown() {
        if (gifModel == null) {
            return;
        }
        saveVote(gifModel.getVote().equals(GifModel.Vote.DOWN) ? GifModel.Vote.NONE : GifModel.Vote.DOWN);
    }

    private void saveVote(GifModel.Vote vote) {
        if (gifModel == null) {
            return;
        }

        voteGifUsecase.saveVote(gifModel, vote)
                .subscribe(gifModel -> watchGifView.showVote(gifModel.getVote()));
    }
}
