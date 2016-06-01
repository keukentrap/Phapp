package haakjeopenen.phapp.structalikes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import haakjeopenen.phapp.R;
import haakjeopenen.phapp.fragments.PostFragment.OnListFragmentInteractionListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PostItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder> {

    private final List<PostItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public PostRecyclerViewAdapter(List<PostItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //holder.mIdView.setText(mValues.get(position).id);
        holder.mTitleView.setText(mValues.get(position).title);
        holder.mContentView.setText(mValues.get(position).content);
        holder.mAuthorView.setText(mValues.get(position).author);
        holder.mDateView.setText(String.format("%te %tb %tY",mValues.get(position).date,mValues.get(position).date,mValues.get(position).date));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mContentView;
        public final TextView mAuthorView;
        public final TextView mDateView;
        public PostItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mContentView = (TextView) view.findViewById(R.id.content);
            mAuthorView = (TextView) view.findViewById(R.id.author);
            mDateView = (TextView) view.findViewById(R.id.date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
