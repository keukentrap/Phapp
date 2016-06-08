package haakjeopenen.phapp.fragments;

import android.app.Fragment;
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

	private WeatherReader weatherreader;

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
		weatherreader = new WeatherReader(this);

		// Inflate the layout for this fragment
		return view;
	}

	public void doneLoading()
	{
		Picasso.with(getActivity()).load(weatherreader.getImage()).resize(370,370).centerCrop().into(imageview); // Original image is 37x37
		weatherinfotext.setText(String.format(getActivity().getString(R.string.weathertemplate), weatherreader.getTemp(), weatherreader.getWind(), weatherreader.getWindspeedMS(), weatherreader.getHumidity(), weatherreader.getPrecipitation()));
	}

}
