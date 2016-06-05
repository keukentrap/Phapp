package haakjeopenen.phapp.fragments.contact;

import android.app.Fragment;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import haakjeopenen.phapp.R;
import haakjeopenen.phapp.net.API;

/**
 * Class to show contact information of Phocas
 */
public class ContactFragment extends Fragment {


	private API api;

	public ContactFragment() {
		api = API.getInstance(null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_contact, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		TextView contacttextview = (TextView) getView().findViewById(R.id.contacttextview);
		contacttextview.setMovementMethod(LinkMovementMethod.getInstance());
		api.loadPageContent("contact", contacttextview);
	}
}
