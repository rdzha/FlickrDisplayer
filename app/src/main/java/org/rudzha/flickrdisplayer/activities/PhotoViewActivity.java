package org.rudzha.flickrdisplayer.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.IntentCompat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.rudzha.flickrdisplayer.R;
import org.rudzha.flickrdisplayer.model.types.Photo;

/**
 * Simple photo details activity
 */
public class PhotoViewActivity extends BaseToolbarActivity {
    private static final String EXTRA_PHOTO = "photo";

    private ImageView photoView;
    private Photo photo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);
        photoView = (ImageView) findViewById(R.id.photo);

        photo = getIntent().getParcelableExtra(EXTRA_PHOTO);
        initToolbar(photo.getTitle());
        ActivityCompat.postponeEnterTransition(this);
        //We need to ensure that image is already displayed at the start of transition animation.
        //This will introduce a slight lag on start of this activity, but the other option is only to have no animation during the first photo viewing.
        Picasso.with(this).load(photo.getUrlSmall()).fit().centerInside().into(photoView, new Callback() {
            @Override
            public void onSuccess() {
                processTransition();
            }

            @Override
            public void onError() {

            }
        });
    }

    private void processTransition() {
        photoView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                photoView.getViewTreeObserver().removeOnPreDrawListener(this);
                ActivityCompat.startPostponedEnterTransition(PhotoViewActivity.this);
                return true;
            }
        });
    }

    public static void launch(Activity activity, View sharedImageView, Photo photo) {
        Intent intent = new Intent(activity, PhotoViewActivity.class);
        intent.putExtra(EXTRA_PHOTO, photo);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedImageView, activity.getString(R.string.image_transition));
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }
}
