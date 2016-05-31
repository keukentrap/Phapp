package haakjeopenen.phapp.nonactivityclasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import haakjeopenen.phapp.R;

/**
 * Created by U on 31-5-2016.
 */
public class ImageAdapter extends BaseAdapter {
	private Context mContext;

	public ImageAdapter(Context c) {
		mContext = c;
	}

	public int getCount() {
		return mThumbIds.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			// if it's not recycled, initialize some attributes
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(240, 180));
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			imageView.setPadding(2, 2, 2, 2);
		} else {
			imageView = (ImageView) convertView;
		}

		imageView.setImageBitmap(mThumbIds.get(position));
		return imageView;
	}

	// references to our images
	//private Integer[] mThumbIds = {
			/*
			R.drawable.sample_2, R.drawable.sample_3,
			R.drawable.sample_4, R.drawable.sample_5,
			R.drawable.sample_6, R.drawable.sample_7,
			R.drawable.sample_0, R.drawable.sample_1,
			R.drawable.sample_2, R.drawable.sample_3,
			R.drawable.sample_4, R.drawable.sample_5,
			R.drawable.sample_6, R.drawable.sample_7,
			R.drawable.sample_0, R.drawable.sample_1,
			R.drawable.sample_2, R.drawable.sample_3,
			R.drawable.sample_4, R.drawable.sample_5,
			R.drawable.sample_6, R.drawable.sample_7
			*/
	//};

	private List<Bitmap> mThumbIds = new ArrayList<Bitmap>();

	/**
	 * Remove all thumbs
	 */
	public void resetThumbs()
	{
		mThumbIds.clear();
	}

	// Temp
	public void addThumb(Bitmap bitmap)
	{
		//mThumbIds.add(R.drawable.sample_3);
		assert(bitmap != null);

		mThumbIds.add(bitmap);
	}
}