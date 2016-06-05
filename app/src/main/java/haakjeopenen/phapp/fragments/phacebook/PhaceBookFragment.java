package haakjeopenen.phapp.fragments.phacebook;


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
import haakjeopenen.phapp.models.User;
import haakjeopenen.phapp.net.API;
import haakjeopenen.phapp.util.Notify;
import haakjeopenen.phapp.widgets.DividerItemDecoration;

/**
 * {@link PhaceBookFragment} to search the user database
 */
public class PhaceBookFragment extends Fragment implements View.OnClickListener, Notify {

    private API api;
    private Button mSearch;
    private EditText mName;
    private RecyclerView mRecyclerView;
    private PhacebookRecyclerViewAdapter adapter;

    private ArrayList<User> results;


    public PhaceBookFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_phacebook, container, false);

        mName = (EditText) view.findViewById(R.id.input_phacebook_field);

        mName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onClick(v);
                    handled = true;
                }
                return handled;
            }
        });

        mSearch = (Button) view.findViewById(R.id.search_phacebook_button);
        mSearch.setOnClickListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.result_view);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setHasFixedSize(true); //performance optimization

        //check if this is the first time loading
        if (adapter == null) {
            results = new ArrayList<>();
            adapter = new PhacebookRecyclerViewAdapter(view.getContext(), results);


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
     * Called when search button has been clicked.
     *
     * @param v The search button that was clicked.
     */
    @Override
    public void onClick(View v) { // TODO: als er meer knoppen nodig zijn, view goed afhandelen
        String search = mName.getText().toString();
        System.out.println("Searching for: " + search);

        api.searchUsers(search, results, this);
    }
}
