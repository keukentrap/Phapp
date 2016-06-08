package haakjeopenen.phapp.fragments.photos;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import haakjeopenen.phapp.R;
import haakjeopenen.phapp.models.Photo;
import haakjeopenen.phapp.widgets.TouchImageView;

/**
 * Created by wietze on 6/6/16.
 */
public class PhotoZoomAdapter extends PagerAdapter {
    private List<Photo> mPhotos = new ArrayList<>();
    private Context mContext;

    public PhotoZoomAdapter(Context c, List<Photo> thumbs) {
        mContext = c;
        mPhotos = thumbs;
    }


    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        if (mPhotos != null)
            return mPhotos.size();
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //create an image the user can pinch zoom
        TouchImageView imageView = new TouchImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setId(R.id.imageView);

        //load full image from website
        Picasso.with(mContext).load(mPhotos.get(position).imgurl).into(imageView);

        //finally, add it
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }


}
