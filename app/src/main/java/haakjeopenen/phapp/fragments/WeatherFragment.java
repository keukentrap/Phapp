package haakjeopenen.phapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import haakjeopenen.phapp.R;
import haakjeopenen.phapp.net.WeatherReader;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeatherFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {
	private OnFragmentInteractionListener mListener;
	private final WeatherReader weatherreader = new WeatherReader(this);

	private ImageView imageview;
	private TextView weatherinfotext;

	public WeatherFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_weather, container, false);

		imageview = (ImageView) view.findViewById(R.id.weatherPicture);
		weatherinfotext = (TextView) view.findViewById(R.id.weatherInfoText);

		// Inflate the layout for this fragment
		return view;
	}

	// TODO: Rename method, update argument and hook method into UI event?
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	public void doneLoading()
	{
		// TODO: A disgrace?
		if(imageview == null)
			System.out.println("CAUTION: ImageView still null");
		else {
			Picasso.with(getActivity()).load(weatherreader.getImage()).into(imageview);
			weatherinfotext.setText(String.format(getActivity().getString(R.string.weathertemplate), weatherreader.getTemp(), weatherreader.getWind()));
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			mListener = (OnFragmentInteractionListener) context;
		} else {
//			throw new RuntimeException(context.toString()
//					+ " must implement OnFragmentInteractionListener");
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
