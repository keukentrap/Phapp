package haakjeopenen.phapp.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import haakjeopenen.phapp.R;
import haakjeopenen.phapp.nonactivityclasses.API;
import haakjeopenen.phapp.nonactivityclasses.DividerItemDecoration;
import haakjeopenen.phapp.nonactivityclasses.PhacebookRecyclerViewAdapter;
import haakjeopenen.phapp.nonactivityclasses.UserItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhaceBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhaceBookFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private API api;
    private TextView mResult;
    private Button mSearch;
    private EditText mName;
    private RecyclerView mRecyclerView;
    private PhacebookRecyclerViewAdapter adapter;

    private ArrayList<UserItem> results;


    public PhaceBookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhaceBookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhaceBookFragment newInstance(String param1, String param2) {
        PhaceBookFragment fragment = new PhaceBookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_phace_book, container, false);

        mName = (EditText) view.findViewById(R.id.input_phacebook_field);
        mSearch = (Button) view.findViewById(R.id.search_phacebook_button);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.result_view);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        mRecyclerView.setHasFixedSize(true);

        results = new ArrayList<>();
        adapter = new PhacebookRecyclerViewAdapter(view.getContext(),results);

        mSearch.setOnClickListener(this);


        api = API.getInstance();


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

        api.searchUsers(search,results,this);
    }
}
