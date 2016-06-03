package haakjeopenen.phapp.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import haakjeopenen.phapp.R;
import haakjeopenen.phapp.nonactivityclasses.API;
import haakjeopenen.phapp.nonactivityclasses.DividerItemDecoration;
import haakjeopenen.phapp.nonactivityclasses.PhacebookRecyclerViewAdapter;
import haakjeopenen.phapp.nonactivityclasses.UserItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhaceBookFragment extends Fragment implements View.OnClickListener {

    private API api;
    private Button mSearch;
    private EditText mName;
    private RecyclerView mRecyclerView;
    private PhacebookRecyclerViewAdapter adapter;

    private ArrayList<UserItem> results;


    public PhaceBookFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_phace_book, container, false);

        mName = (EditText) view.findViewById(R.id.input_phacebook_field);

        mName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				boolean handled = false;
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					onClick(v); // TODO: als er meer knoppen nodig zijn, view goed afhandelen
					handled = true;
				}
				return handled;
			}
		});

        mSearch = (Button) view.findViewById(R.id.search_phacebook_button);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.result_view);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        mRecyclerView.setHasFixedSize(true);

        //check if this is the first time loading
        if (adapter == null) {
            results = new ArrayList<>();
            adapter = new PhacebookRecyclerViewAdapter(view.getContext(), results);

            mSearch.setOnClickListener(this);
            api = API.getInstance();
        } else {
            mRecyclerView.setAdapter(adapter);
        }

        return view;

    }

    public void notifyUpdate() {
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        String search = mName.getText().toString();
        System.out.println("Searching for: " + search);

        api.searchUsers(search, results, this);
    }
}
