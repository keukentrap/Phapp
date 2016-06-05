package haakjeopenen.phapp.fragments.photos;

import android.view.View;

import haakjeopenen.phapp.models.Photo;

/**
 * Created by wietze on 6/2/16.
 */
public interface PhotoZoomListener {
    void onPhotoZoom(final View thumbView, Photo photo);
}
