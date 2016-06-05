package haakjeopenen.phapp.ui.photos;

import android.view.View;

import haakjeopenen.phapp.models.PhotoItem;

/**
 * Created by wietze on 6/2/16.
 */
public interface PhotoZoomListener {
    void onPhotoZoom(final View thumbView, PhotoItem photoItem);
}
