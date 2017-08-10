package com.ra1ph.giphysample.data.source.remote;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mikhail Korshunov on 09.08.17.
 */

public class GiphyPagination {
    @SerializedName("offset")
    private int offset;
    @SerializedName("total_count")
    private int totalCount;

    public int getOffset() {
        return offset;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
