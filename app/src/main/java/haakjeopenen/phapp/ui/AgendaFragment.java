package haakjeopenen.phapp.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import haakjeopenen.phapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class AgendaFragment extends Fragment implements CalendarView.OnDateChangeListener {

    private TextView mDateInfo;
    private CalendarView mCalender;

    public AgendaFragment() {
        // Required empty public constructor
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_agenda, container, false);

		mCalender = (CalendarView) view.findViewById(R.id.calendarView);
		mCalender.setOnDateChangeListener(this);
		mDateInfo = (TextView) view.findViewById(R.id.dateInfo);

		return view;
	}

    @Override
    public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
		mDateInfo.setText(i2 + "/" + i1 + "/" + i);
	}
}
