package org.rudzha.flickrdisplayer.model;


import org.rudzha.flickrdisplayer.model.types.PaginatedData;
import org.rudzha.flickrdisplayer.model.types.Photo;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface FlickrService {
    @GET("?method=flickr.interestingness.getList")
    Observable<ResponseBody> getInterestingPhotosObservable(
            @Query("api_key") String apiKey,
            @Query("extras") String extras,
            @Query("format") String format,
            @Query("nojsoncallback") int noJsonCallback,
            @Query("page")int page
    );

    @GET("?method=flickr.photos.getInfo")
    Observable<ResponseBody> getPhotoObservable(
            @Query("api_key") String apiKey,
            @Query("photo_id") String photoId,
            @Query("format") String format,
            @Query("nojsoncallback") int noJsonCallback,
            @Query("page")int page
    );

    @GET("?method=flickr.photos.search")
    Observable<ResponseBody> getSearchgPhotosObservable(
            @Query("api_key") String apiKey,
            @Query("text") String text,
            @Query("extras") String extras,
            @Query("format") String format,
            @Query("nojsoncallback") int noJsonCallback,
            @Query("page")int page
    );
}
