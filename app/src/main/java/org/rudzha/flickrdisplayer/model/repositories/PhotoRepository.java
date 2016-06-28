package org.rudzha.flickrdisplayer.model.repositories;

import org.rudzha.flickrdisplayer.model.types.PaginatedData;
import org.rudzha.flickrdisplayer.model.types.Photo;

import java.util.List;

import rx.Observable;

/**
 * Provides accessor methods for Photo type.
 * This interface is designed as an abstraction layer to allow for a more manageable code structure.
 * For example, Retrofit-based implementation could be easily swapped for more classical HttpUrlConnection-based one(it makes sense to do so, considering how Flickr API structured.
 * Also, it is very easy to mock this component or to extend it logic using OOP design pattern, for example to add caching layer to already existing networking one.
 */
public interface PhotoRepository {
    Observable<PaginatedData<Photo>> getFeaturedPhotos(int page);
    Observable<PaginatedData<Photo>> getPhotos(String query, int page);
    Observable<Photo> getPhoto(String photoId);
}
