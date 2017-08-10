package com.ra1ph.giphysample.data.source.remote;

import com.google.gson.annotations.SerializedName;
import com.ra1ph.giphysample.search.domain.GifModel;

import java.util.List;

/**
 * Created by Mikhail Korshunov on 09.08.17.
 */

public class GiphyGifEntity {
    @SerializedName("id")
    String giphyId;
    @SerializedName("images")
    Images images;

    public static class Images {
        @SerializedName("fixed_width_still")
        Image fixed_width_still;
        @SerializedName("original")
        Image original;
    }

    public static class Image {
        @SerializedName("url")
        String url;
        @SerializedName("width")
        int width;
        @SerializedName("height")
        int height;
        @SerializedName("mp4")
        String mp4;
    }

    public String getGiphyId() {
        return giphyId;
    }

    public GifModel toGifModel(GifModel.Vote vote) {
        GifModel gifModel = new GifModel(giphyId, images.fixed_width_still.url, images.original.mp4, vote);
        return gifModel;
    }
}
