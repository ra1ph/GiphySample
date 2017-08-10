package com.ra1ph.giphysample.data.source.remote;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mikhail Korshunov on 09.08.17.
 */

public interface GiphyApi {
    @GET("v1/gifs/search")
    Observable<GiphyGifSearchResponse> searchGifs(@Query("api_key") String apiKey,
                                                @Query("q") String query,
                                                @Query("limit") int limit,
                                                  @Query("offset") int offset);
}
