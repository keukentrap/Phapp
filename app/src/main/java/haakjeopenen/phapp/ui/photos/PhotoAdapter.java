package haakjeopenen.phapp.ui.photos;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import haakjeopenen.phapp.models.PhotoItem;

/**
 * {@link BaseAdapter} to display photos in the {@link PhotosFragment}
 */
public class PhotoAdapter extends BaseAdapter {
	private final Context mContext;
	private final PhotoZoomListener mListener;
	private List<PhotoItem> mThumbs = new ArrayList<>();

	public PhotoAdapter(Context c, ArrayList<PhotoItem> thumbs, PhotoZoomListener listener) {
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
			imageView = new ImageView(mContext);
			imageView.setAdjustViewBounds(true);
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
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