package haakjeopenen.phapp.nonactivityclasses;

import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import haakjeopenen.phapp.R;
import haakjeopenen.phapp.fragments.PhotosFragment;
import haakjeopenen.phapp.fragments.PostFragment;

/**
 * Created by U on 31-5-2016.
 */
public class ImageAdapter extends BaseAdapter {
	private final Context mContext;
	private List<ImageInfo> mThumbs = new ArrayList<>();
	private final PhotoZoomListener mListener;

	public ImageAdapter(Context c, ArrayList<ImageInfo> thumbs, PhotoZoomListener listener) {
		mContext = c;
		mThumbs = thumbs;
		mListener = listener;
	}

	public int getCount() {
		return mThumbs.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ImageView imageView;

		if (convertView == null) {
			// if it's not recycled, initialize some attributes
			// imageView = (ImageView) parent.findViewById(R.id.post_image);
			imageView = new ImageView(mContext);
			//imageView.setLayoutParams(new GridView.LayoutParams(300 , 300));
			imageView.setAdjustViewBounds(true);
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			//imageView.setPadding(2, 2, 2, 2);
		} else {
			imageView = (ImageView) convertView;
		}

		String thumburl = mThumbs.get(position).thumburl;
		Picasso.with(mContext).load(thumburl).into(imageView);

		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onPhotoZoom(imageView,mThumbs.get(position));
			}
		});
		return imageView;
	}

	/**
	 * Remove all thumbs
	 */
	public void resetThumbs()
	{
		mThumbs.clear();
	}

}