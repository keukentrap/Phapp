package haakjeopenen.phapp.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import haakjeopenen.phapp.R;
import haakjeopenen.phapp.net.WeatherReader;

/**
 * Fragment for the weather
 */
public class WeatherFragment extends Fragment {
	private OnFragmentInteractionListener mListener;
	private WeatherReader weatherreader;

	private ImageView imageview;
	private TextView weatherinfotext;

	public WeatherFragment() {

	}

//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_weather, container, false);

		imageview = (ImageView) view.findViewById(R.id.weatherPicture);
		//imageview.setScaleType(null

		weatherinfotext = (TextView) view.findViewById(R.id.weatherInfoText);

		weatherreader = new WeatherReader(this);

		// Inflate the layout for this fragment
		return view;
	}

	public void doneLoading()
	{
		Picasso.with(getActivity()).load(weatherreader.getImage()).resize(370,370).centerCrop().into(imageview); // Original image is 37x37
		weatherinfotext.setText(String.format(getActivity().getString(R.string.weathertemplate), weatherreader.getTemp(), weatherreader.getWind(), weatherreader.getWindspeedMS(), weatherreader.getHumidity(), weatherreader.getPrecipitation()));
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			mListener = (OnFragmentInteractionListener) context;
		} else {
//			throw new RuntimeException(context.toString()
//					+ " must implement OnPageSelectedListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p/>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name?
		void onFragmentInteraction(Uri uri);
	}
}
