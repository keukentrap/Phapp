package haakjeopenen.phapp.fragments.photos;

import android.app.Fragment;
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
 * {@link OnPageSelectedListener} interface
 * to handle interaction events.
 */
public class PhotoHighlightedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int position;
    private List<Photo> images;

    private OnPageSelectedListener mListener;

    public PhotoHighlightedFragment() {
        // Required empty public constructor
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setImages(List<Photo> images) {
        this.images = images;
    }

    public void setOnFragmentInteractionListener(OnPageSelectedListener listener) {
        this.mListener = listener;
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

        final ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                System.out.println("Photo selected");
                mListener.onPageSelected(images.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(position);

        return view;
    }

    public interface OnPageSelectedListener {
        void onPageSelected(Photo photo);
    }
}
