package haakjeopenen.phapp.fragments;

import android.animation.Animator;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import haakjeopenen.phapp.nonactivityclasses.API;
import haakjeopenen.phapp.nonactivityclasses.MultiSwipeRefreshLayout;
import haakjeopenen.phapp.nonactivityclasses.PostRecyclerViewAdapter;
import haakjeopenen.phapp.R;
import haakjeopenen.phapp.nonactivityclasses.PostItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PostFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private OnListFragmentInteractionListener mListener;
    private MultiSwipeRefreshLayout multiSwipeRefreshLayout;
    private PostRecyclerViewAdapter adapter;


    private RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PostFragment() {
    }

    @SuppressWarnings("unused")
    public static PostFragment newInstance(int columnCount) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);

        multiSwipeRefreshLayout = (MultiSwipeRefreshLayout) view;
        multiSwipeRefreshLayout.setSwipeableChildren(R.id.list);
        multiSwipeRefreshLayout.setOnRefreshListener(this);

        ArrayList<PostItem> list = new ArrayList<>();

        API api = API.getInstance();
        api.loadLatestPosts(list,this);

        adapter =  new PostRecyclerViewAdapter(list, mListener);


        return view;
    }

    public void notifyUpdatePosts() {
        multiSwipeRefreshLayout.setRefreshing(false);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        System.out.println("Refreshing");
        ArrayList<PostItem> list = new ArrayList<>();
        adapter = new PostRecyclerViewAdapter(list,mListener);
        API.getInstance().loadLatestPosts(list,this);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other haakjeopenen.phapp.fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(PostItem item);
    }
}
