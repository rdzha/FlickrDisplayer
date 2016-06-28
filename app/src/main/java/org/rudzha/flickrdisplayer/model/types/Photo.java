package org.rudzha.flickrdisplayer.model.types;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Plain immutable type for photos.
 */
public class Photo implements Comparable<Photo>, Parcelable {
    private final String id;
    private final String authorName;
    private final String title;
    private final String description;
    private final String urlSmall;
    private final String urlLarge;
    private final Date uploadDate;

    public Photo(String id, String authorName, String title, String description, String urlSmall, String urlLarge, Date uploadDate) {
        this.id = id;
        this.authorName = authorName;
        this.title = title;
        this.description = description;
        this.urlSmall = urlSmall;
        this.urlLarge = urlLarge;
        this.uploadDate = uploadDate;
    }

    protected Photo(Parcel in) {
        id = in.readString();
        authorName = in.readString();
        title = in.readString();
        description = in.readString();
        urlSmall = in.readString();
        urlLarge = in.readString();
        uploadDate = new Date(in.readLong());
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public String getAuthorName() {
        return authorName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrlSmall() {
        return urlSmall;
    }

    public String getUrlLarge() {
        return urlLarge;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int compareTo(Photo photo) {
        return this.id.compareTo(photo.id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(authorName);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(urlSmall);
        parcel.writeString(urlLarge);
        parcel.writeLong(uploadDate.getTime());
    }
}
