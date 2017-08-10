package com.ra1ph.giphysample.data.source.local;

import android.support.annotation.Nullable;

import com.ra1ph.giphysample.search.domain.GifModel;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Mikhail Korshunov on 09.08.17.
 */

public class VotesSourceImplementation implements VotesSource {
    private Box<VoteEntity> voteBox;

    private BehaviorSubject<Long> upVotesNumber = BehaviorSubject.create();
    private BehaviorSubject<Long> downVotesNumber = BehaviorSubject.create();

    public VotesSourceImplementation(BoxStore boxStore) {
        checkNotNull(boxStore);
        voteBox = boxStore.boxFor(VoteEntity.class);
        reloadCounters();
    }

    @Override
    public void vote(String gilphyGifId, GifModel.Vote vote) {
        checkNotNull(gilphyGifId);
        VoteEntity voteEntity = voteEntityForGilphyGif(gilphyGifId);
        if (voteEntity == null) {
            voteEntity = new VoteEntity(gilphyGifId);
        }
        if (voteEntity.getVote().equals(vote)) { //already the same or not exist
            return;
        }

        voteEntity.setVote(vote);
        voteBox.put(voteEntity);
        reloadCounters();
    }

    @Override
    public Observable<Long> votesUpCount() {
        return upVotesNumber;
    }

    @Override
    public Observable<Long> votesDownCount() {
        return downVotesNumber;
    }

    @Override
    public GifModel.Vote voteForGilphyGif(String gilphyGifId) {
        checkNotNull(gilphyGifId);
        VoteEntity voteEntity = voteEntityForGilphyGif(gilphyGifId);
        if (voteEntity == null) {
            voteEntity = new VoteEntity(gilphyGifId);
            voteBox.put(voteEntity);
        }
        return voteEntity.getVote();
    }

    @Nullable
    private VoteEntity voteEntityForGilphyGif(String gilphyGifId) {
        checkNotNull(gilphyGifId);
        return voteBox.query()
                .equal(VoteEntity_.giphyId, gilphyGifId)
                .build()
                .findFirst();
    }

    private long loadVotesCount(GifModel.Vote vote) {
        checkNotNull(vote);
        return voteBox.query()
                .equal(VoteEntity_.vote, vote.ordinal())
                .build()
                .count();
    }

    private void reloadCounters() {
        upVotesNumber.onNext(loadVotesCount(GifModel.Vote.UP));
        downVotesNumber.onNext(loadVotesCount(GifModel.Vote.DOWN));
    }
}
