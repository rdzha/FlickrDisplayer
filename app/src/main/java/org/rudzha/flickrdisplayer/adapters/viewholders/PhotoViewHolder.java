package org.rudzha.flickrdisplayer.adapters.viewholders;

import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.rudzha.flickrdisplayer.R;
import org.rudzha.flickrdisplayer.model.types.Photo;

import java.text.DateFormat;

/**
 * ViewHolder for Photos
 */
public class PhotoViewHolder extends AbsViewHolder<Photo> {
    private final static DateFormat UPLOAD_DATE_FORMAT = DateFormat.getDateTimeInstance();
    private final ImageView photoView;
    private final TextView title;
    private final TextView description;
    private final TextView authorName;
    private final TextView uploadDate;

    public PhotoViewHolder(View itemView, final ClickListener clickListener) {
        super(itemView);
        photoView = (ImageView) itemView.findViewById(R.id.photo);
        title = (TextView) itemView.findViewById(R.id.title);
        description = (TextView) itemView.findViewById(R.id.description);
        authorName = (TextView) itemView.findViewById(R.id.author);
        uploadDate = (TextView) itemView.findViewById(R.id.date);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClicked(getAdapterPosition(), photoView);
            }
        });
    }

    @Override
    public void bind(final Photo item) {
        final int width = photoView.getWidth();
        //Check if view already was measured and if it is not, load image after that.
        //TODO: assign photo dimensions to imageview immediately, to reduce the effect of 'jumpy' photos in RecyclerView
        if(width > 0)
            Picasso.with(photoView.getContext()).load(item.getUrlSmall()).resize(width, 0).into(photoView);
        else
            photoView.post(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(photoView.getContext()).load(item.getUrlSmall()).resize(photoView.getWidth(), 0).into(photoView);
                }
            });
        title.setText(item.getTitle());
        description.setText(getRichText(item.getDescription()));
        authorName.setText(item.getAuthorName());
        uploadDate.setText(UPLOAD_DATE_FORMAT.format(item.getUploadDate()));
    }

    /**
     * Utility method for SDK 24 compatibility.
     * Currently there are no Compat APIs designed to handle this, so this small method is necessary.
     * @param htmlText String containing text with html markup
     * @return Spanned text with proper markup handling
     */
    private Spanned getRichText(String htmlText) {
        Spanned richText;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            richText = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY);
        } else {
            richText = Html.fromHtml(htmlText);
        }
        return richText;
    }
}
