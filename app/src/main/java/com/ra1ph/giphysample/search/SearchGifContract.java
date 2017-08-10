package com.ra1ph.giphysample.search;

import com.ra1ph.giphysample.search.domain.GifModel;

import java.util.List;

/**
 * Created by Mikhail Korshunov on 09.08.17.
 */

public interface SearchGifContract {
    interface View {
        void showGifs(List<GifModel> gifs);
        void showNothingFound();
        void showEmptyQuery();
        void showError(String errorMessage);
        void showGif(GifModel gifModel);
        void showUpThumbsCount(long count);
        void showDownThumbsCount(long count);
    }

    interface Presenter {
        void bindView();
        void unbindView();
        void seekGif(String query);
        void loadMore();
        void gifSelected(GifModel gifModel);
    }
}
