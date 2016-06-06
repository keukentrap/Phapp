package haakjeopenen.phapp.fragments.photos;

import java.util.List;

import haakjeopenen.phapp.models.Photo;

/**
 * Created by wietze on 6/2/16.
 */
public interface PhotoZoomListener {
    void onPhotoZoom(List<Photo> images, int position);
}
