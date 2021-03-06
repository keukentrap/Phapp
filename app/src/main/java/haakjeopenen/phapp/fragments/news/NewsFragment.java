package haakjeopenen.phapp.fragments.news;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import haakjeopenen.phapp.R;
import haakjeopenen.phapp.models.Post;
import haakjeopenen.phapp.net.API;
import haakjeopenen.phapp.util.Notify;
import haakjeopenen.phapp.widgets.MultiSwipeRefreshLayout;

/**
 * Fragment representing a list with the News items of the Phocas site
 * <p/>
 */
public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Notify {

    private MultiSwipeRefreshLayout multiSwipeRefreshLayout;
    private NewsRecyclerViewAdapter adapter;
    private ArrayList<Post> newsList;

    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingBar;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);

        multiSwipeRefreshLayout = (MultiSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        multiSwipeRefreshLayout.setSwipeableChildren(R.id.list);
        multiSwipeRefreshLayout.setOnRefreshListener(this);

        mLoadingBar = (ProgressBar) view.findViewById(R.id.progressBar);

        //check if this is the first time loading
        if (adapter == null) {
            newsList = new ArrayList<>();

            API api = API.getInstance();
            api.loadNews(newsList, this);

            adapter = new NewsRecyclerViewAdapter(newsList);
        } else {
            notifyFinished();
        }

        return view;
    }

    @Override
    public void onRefresh() {
        System.out.println("Refreshing: news");
        API.getInstance().loadNews(newsList, this);
    }

    @Override
    public void notifyUpdate() {
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void notifyFinished() {
        multiSwipeRefreshLayout.setRefreshing(false);
        mLoadingBar.setVisibility(View.GONE);
        mRecyclerView.setAdapter(adapter);

    }
}
