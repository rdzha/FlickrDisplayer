package org.rudzha.flickrdisplayer.model.repositories;

import org.json.JSONException;
import org.rudzha.flickrdisplayer.model.FlickrService;
import org.rudzha.flickrdisplayer.model.ResponseMapper;
import org.rudzha.flickrdisplayer.model.types.PaginatedData;
import org.rudzha.flickrdisplayer.model.types.Photo;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * This implementation relies on Retrofit and completely encapsulates it inside.
 * This class also manages data flow and error handling.
 */
public class PhotoRepositoryImpl implements PhotoRepository {
    private static final String API_URL = "https://api.flickr.com/services/rest/";
    private static final String API_KEY = "7cd7923d23ce0beb5231a16bf538979b";
    private static final String FORMAT_REST = "json";
    private static final String PHOTO_EXTRAS = "description,url_m,url_l,owner_name,date_upload";

    private final FlickrService flickrService;
    private final ResponseMapper responseMapper;

    public PhotoRepositoryImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        flickrService = retrofit.create(FlickrService.class);
        responseMapper = new ResponseMapper();
    }


    @Override
    public Observable<PaginatedData<Photo>> getFeaturedPhotos(int page) {
        return flickrService.getInterestingPhotosObservable(API_KEY, PHOTO_EXTRAS, FORMAT_REST, 1, page).map(new Func1<ResponseBody, PaginatedData<Photo>>() {
            @Override
            public PaginatedData<Photo> call(ResponseBody responseBody) {
                try {
                    return responseMapper.toPhotoPage(responseBody);
                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<PaginatedData<Photo>> getPhotos(String query, int page) {
        return flickrService.getSearchgPhotosObservable(API_KEY, query, PHOTO_EXTRAS, FORMAT_REST, 1, page).map(new Func1<ResponseBody, PaginatedData<Photo>>() {
            @Override
            public PaginatedData<Photo> call(ResponseBody responseBody) {
                try {
                    return responseMapper.toPhotoPage(responseBody);
                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Photo> getPhoto(String photoId) {
        return null;
    }
}
