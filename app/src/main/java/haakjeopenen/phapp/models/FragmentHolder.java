package haakjeopenen.phapp.models;

import android.app.Fragment;

/**
 * Created by U on 7-6-2016.
 */
public class FragmentHolder {
	public String title;
	public Fragment fragment;

	public FragmentHolder(String title, Fragment fragment)
	{
		this.title = title;
		this.fragment = fragment;
	}
}
