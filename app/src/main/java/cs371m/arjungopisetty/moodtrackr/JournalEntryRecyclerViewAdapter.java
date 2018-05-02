package cs371m.arjungopisetty.moodtrackr;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cs371m.arjungopisetty.moodtrackr.JournalFragment.OnListFragmentInteractionListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class JournalEntryRecyclerViewAdapter extends RecyclerView.Adapter<JournalEntryRecyclerViewAdapter.ViewHolder> {

    private List<FirebaseRecord> firebaseRecords;
    private final OnListFragmentInteractionListener mListener;

    private DateFormat formatter;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mContentView;
        public FirebaseRecord mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.content_pic);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public JournalEntryRecyclerViewAdapter(List<FirebaseRecord> items, OnListFragmentInteractionListener listener) {
        firebaseRecords = items;
        mListener = listener;

        formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_journal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = firebaseRecords.get(position);
        String time = formatter.format(new Date(holder.mItem.time));
        holder.mContentView.setText(time);
        //holder.mContentView.setText(mValues.get(position).content);
        holder.mImageView.setImageResource(MoodColors.colorMap.get(holder.mItem.tones.get(0).tone_id));

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
        return firebaseRecords.size();
    }

    public void swap(List<FirebaseRecord> newData) {
        if (newData == null || newData.size() == 0) {
            return;
        }
        if (newData != null && newData.size() > 0) {
            firebaseRecords.clear();
        }
        firebaseRecords.addAll(newData);
        notifyDataSetChanged();
    }
}
