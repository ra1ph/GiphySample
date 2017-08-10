package com.ra1ph.giphysample.data.source.remote;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by Mikhail Korshunov on 09.08.17.
 */

public class GiphyMeta {
    @SerializedName("msg")
    private String message; //should be OK
    @SerializedName("code")
    private int code; //should be 200

    public boolean isOk() {
        return message.equals("OK");
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Code %d: %s", code, message);
    }
}
