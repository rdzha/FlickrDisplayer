package org.rudzha.flickrdisplayer.model;

import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.rudzha.flickrdisplayer.model.types.PaginatedData;
import org.rudzha.flickrdisplayer.model.types.Photo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;

/**
 * This class maps response bodies to objects.
 * This could be done automatically with libraries like Gson/Jackson/etc,
 * but manual approach has a few pros:
 * 1)It doesn't rely on any class/field names, as Reflections are not used.
 * 2)It gives us more control in terms of paging/errors/general data handling.
 * 3)It saves us a few thousand methods from that fancy Json converter libraries.
 * 4)This is a tiny project, so this approach doesn't really have any cons.
 */
public class ResponseMapper {
    public PaginatedData<Photo> toPhotoPage(ResponseBody responseBody) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject(responseBody.string());
        FlickrApiException exception = getException(jsonObject);
        if(exception != null)
            throw exception;
        JSONObject photosObject = jsonObject.getJSONObject("photos");
        int page = photosObject.getInt("page");
        int pages = photosObject.getInt("pages");
        int perPage = photosObject.getInt("perpage");
        int total = photosObject.getInt("total");
        List<Photo> photoList = new ArrayList<>();
        JSONArray photosArray = photosObject.getJSONArray("photo");
        for(int i = 0; i < photosArray.length(); i++) {
            JSONObject photoObject = photosArray.getJSONObject(i);
            String id = photoObject.getString("id");
            String authorName = photoObject.getString("ownername");
            String title = photoObject.getString("title");
            String description = photoObject.getJSONObject("description").getString("_content");
            String urlSmall = photoObject.has("url_m") ? photoObject.getString("url_m") : null;
            String urlLarge = photoObject.has("url_l") ? photoObject.getString("url_l") : null;
            long uploadDate = photoObject.getLong("dateupload");
            photoList.add(new Photo(id, authorName, title, description, urlSmall, urlLarge, new Date(uploadDate * 1000)));
        }
        return new PaginatedData<>(page, pages, photoList);
    }

    private @Nullable FlickrApiException getException(JSONObject rootObject) throws JSONException {
        String stat = rootObject.getString("stat");
        if(stat.equals("fail")) {
            int errorCode = rootObject.getInt("code");
            String message = rootObject.getString("message");
            return new FlickrApiException(errorCode, message);
        }
        else
            return null;
    }
}
