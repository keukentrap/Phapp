package haakjeopenen.phapp.fragments.photos;

import haakjeopenen.phapp.models.Photo;

/**
 * Listener to call when Highlighted photo in {@link PhotoHighlightedFragment} changes
 */
public interface OnPhotoHighlightedListener {
    void onPhotoHighlighted(Photo photo);
}