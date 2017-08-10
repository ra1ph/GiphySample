package com.ra1ph.giphysample.data.source.remote;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mikhail Korshunov on 09.08.17.
 */

public class GiphyGifSearchResponse {
    @SerializedName("meta")
    private GiphyMeta meta;

    @Nullable
    @SerializedName("data")
    private List<GiphyGifEntity> data;

    @Nullable
    @SerializedName("pagination")
    private GiphyPagination pagination;

    public GiphyMeta getMeta() {
        return meta;
    }

    @Nullable
    public List<GiphyGifEntity> getData() {
        return data;
    }

    @Nullable
    public GiphyPagination getPagination() {
        return pagination;
    }
}
