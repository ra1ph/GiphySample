package com.ra1ph.giphysample.watch;

import com.ra1ph.giphysample.search.domain.GifModel;

/**
 * Created by Mikhail Korshunov on 10.08.17.
 */

public interface WatchGifContract {
    interface View {
        void playMp4Video(String url);
        void showVote(GifModel.Vote vote);
    }

    interface Presenter {
        void setModel(GifModel gifModel);
        void thumbUp();
        void thumbDown();
    }
}
