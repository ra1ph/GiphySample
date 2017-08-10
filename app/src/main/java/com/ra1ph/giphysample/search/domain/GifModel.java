package com.ra1ph.giphysample.search.domain;

import java.io.Serializable;

/**
 * Created by Mikhail Korshunov on 09.08.17.
 */

public class GifModel implements Serializable{
    public enum Vote {
        NONE, UP, DOWN
    }

    private String gilfyGifId;
    private String previewUrl;
    private String mp4Url;
    private Vote vote;

    public GifModel(String gilfyGifId, String previewUrl, String mp4Url, Vote vote) {
        this.gilfyGifId = gilfyGifId;
        this.previewUrl = previewUrl;
        this.mp4Url = mp4Url;
        this.vote = vote;
    }

    public String getGilfyGifId() {
        return gilfyGifId;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public String getMp4Url() {
        return mp4Url;
    }
}
