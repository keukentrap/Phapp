package haakjeopenen.phapp.ui.news;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import haakjeopenen.phapp.R;
import haakjeopenen.phapp.models.NewsItem;
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
    private ArrayList<NewsItem> newsList;

    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);

        multiSwipeRefreshLayout = (MultiSwipeRefreshLayout) view;
        multiSwipeRefreshLayout.setSwipeableChildren(R.id.list);
        multiSwipeRefreshLayout.setOnRefreshListener(this);

        //check if this is the first time loading
        if (adapter == null) {
            newsList = new ArrayList<>();

            API api = API.getInstance();
            api.loadNews(newsList, this);

            adapter = new NewsRecyclerViewAdapter(newsList);
        } else {
            mRecyclerView.setAdapter(adapter);
        }

        return view;
    }

    @Override
    public void onRefresh() {
        System.out.println("Refreshing: news");
        //TODO can we comment this out?
        //ArrayList<NewsItem> list = new ArrayList<>();
        //adapter = new NewsRecyclerViewAdapter(list);
        API.getInstance().loadNews(newsList, this);
    }

    @Override
    public void notifyUpdate() {
        multiSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setAdapter(adapter);
    }
}
