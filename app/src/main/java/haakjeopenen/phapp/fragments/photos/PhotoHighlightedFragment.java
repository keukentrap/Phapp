package haakjeopenen.phapp.fragments.photos;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import haakjeopenen.phapp.R;
import haakjeopenen.phapp.models.Photo;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotoHighlightedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PhotoHighlightedFragment extends Fragment {
    private int position;
    private List<Photo> images;

    private OnFragmentInteractionListener mListener;

    public PhotoHighlightedFragment() {
        // Required empty public constructor
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setImages(List<Photo> images) {
        this.images = images;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo_highlighted, container, false);

        PhotoZoomAdapter adapter = new PhotoZoomAdapter(view.getContext(), images);

//        AdapterViewFlipper mAdapterViewFlipper = (AdapterViewFlipper) view.findViewById(R.id.adapterViewFlipper);
//        mAdapterViewFlipper.setAdapter(adapter);
//
//        mAdapterViewFlipper.startFlipping();

        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);

        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(position);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
