package org.rudzha.flickrdisplayer.model;

/**
 * Custom exception class to handle Flickr API standardized errors.
 * Can be extended further to provide properly localized messages,
 * for example by using application context.
 */
public class FlickrApiException extends RuntimeException {
    private final int code;

    public FlickrApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
        //TODO: Handle localization here
    }
}
