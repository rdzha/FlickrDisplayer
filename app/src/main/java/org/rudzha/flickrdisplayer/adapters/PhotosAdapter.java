package org.rudzha.flickrdisplayer.adapters;

import android.support.annotation.LayoutRes;
import android.view.View;

import org.rudzha.flickrdisplayer.R;
import org.rudzha.flickrdisplayer.adapters.viewholders.AbsViewHolder;
import org.rudzha.flickrdisplayer.adapters.viewholders.PhotoViewHolder;
import org.rudzha.flickrdisplayer.model.types.Photo;

/**
 * Universal Photo adapter
 */
public class PhotosAdapter extends BaseAdapter<PhotoViewHolder, Photo> implements AbsViewHolder.ClickListener {
    private final Listener listener;

    public PhotosAdapter(Listener listener) {
        super(Photo.class, R.layout.item_photo);
        this.listener = listener;
    }

    @Override
    public PhotoViewHolder createViewHolder(View itemView) {
        return new PhotoViewHolder(itemView, this);
    }

    @Override
    public void onClicked(int position, View view) {
        listener.onPhotoClicked(getItem(position), view);
    }

    public interface Listener {
        void onPhotoClicked(Photo photo, View view);
    }
}
