package com.ra1ph.giphysample.search.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikhail Korshunov on 10.08.17.
 */

public class GifsList {
    private List<GifModel> gifModels;
    private int offset;
    private int totalCount;

    public GifsList() {
        gifModels = new ArrayList<>();
        offset = 0;
        totalCount = 0;
    }

    public GifsList(List<GifModel> gifModels, int offset, int totalCount) {
        this.gifModels = gifModels;
        this.offset = offset;
        this.totalCount = totalCount;
    }

    public List<GifModel> getGifModels() {
        return gifModels;
    }

    public int getNextOffset() {
        return offset + gifModels.size();
    }

    public boolean hasMore() {
        return totalCount > getNextOffset();
    }
}
