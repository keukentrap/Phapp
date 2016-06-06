package haakjeopenen.phapp.fragments.photos;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import haakjeopenen.phapp.models.Photo;
import haakjeopenen.phapp.widgets.TouchImageView;

/**
 * Created by wietze on 6/6/16.
 */
public class PhotoZoomAdapter extends PagerAdapter {
    private List<Photo> mThumbs = new ArrayList<>();
    private Context mContext;

    public PhotoZoomAdapter(Context c, List<Photo> thumbs) {
        mContext = c;
        mThumbs = thumbs;
    }


    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        if (mThumbs != null)
            return mThumbs.size();
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TouchImageView imageView = new TouchImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Picasso.with(mContext).load(mThumbs.get(position).imgurl).into(imageView);
        imageView.setBackgroundColor(Color.BLACK);
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }


}
