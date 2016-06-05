package haakjeopenen.phapp.fragments.phacebook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import haakjeopenen.phapp.R;
import haakjeopenen.phapp.models.Post;
import haakjeopenen.phapp.models.User;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Post}.
 */
public class PhacebookRecyclerViewAdapter extends RecyclerView.Adapter<PhacebookRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValues;
    private final Context mContext;

    public PhacebookRecyclerViewAdapter(Context c, List<User> items) {
        mValues = items;
        mContext = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_phacebook_item, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mName.setText(mValues.get(position).name);
        Picasso.with(mContext).load(mValues.get(position).avatarUrl).into(holder.mAvatar);

        //TODO use it or remove onclickListener
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final ImageView mAvatar;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.phacebook_name);
            mAvatar = (ImageView) view.findViewById(R.id.phacebook_avatar);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }
    }
}
