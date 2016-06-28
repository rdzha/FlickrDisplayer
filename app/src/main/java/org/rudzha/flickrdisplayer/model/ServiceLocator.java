package org.rudzha.flickrdisplayer.model;

import org.rudzha.flickrdisplayer.model.repositories.PhotoRepository;
import org.rudzha.flickrdisplayer.model.repositories.PhotoRepositoryImpl;

/**
 * Provides model layer components to activities. Used to define interfaces implementation and their instancing options.
 * Also acts as a gateway to feed activities with mock components.
 */
public class ServiceLocator {
    private static ServiceLocator serviceLocator;
    private PhotoRepository photoRepository;

    public static ServiceLocator getInstance() {
        if(serviceLocator == null)
            serviceLocator = new ServiceLocator();
        return serviceLocator;
    }

    public PhotoRepository getPhotoRepository() {
        if(photoRepository == null)
            photoRepository = new PhotoRepositoryImpl();
        return photoRepository;
    }
}
