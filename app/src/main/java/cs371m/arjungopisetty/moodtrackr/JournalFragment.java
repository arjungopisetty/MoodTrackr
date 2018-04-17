package cs371m.arjungopisetty.moodtrackr;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class JournalFragment extends Fragment implements ToneParser.FetchFirebaseCallback {

    private OnListFragmentInteractionListener mListener;
    private FirebaseReader mReader;

    private List<FirebaseRecord> firebaseRecords;

    private RecyclerView recyclerView;
    private JournalEntryRecyclerViewAdapter recyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public JournalFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static JournalFragment newInstance() {
        JournalFragment fragment = new JournalFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journalentry_list, container, false);
        firebaseRecords = new ArrayList<>();
        mReader = new FirebaseReader(this);
        mReader.fetchFromFirebase();

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerViewAdapter = new JournalEntryRecyclerViewAdapter(firebaseRecords, mListener);
            recyclerView.setAdapter(recyclerViewAdapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onComplete(List<FirebaseRecord> records) {
//        firebaseRecords = new ArrayList<>();
//        for (int i = 0; i < records.size(); i++) {
//            firebaseRecords.add(records.get(i).journalEntry);
//        }
        Log.d(MainActivity.TAG, "OnComplete()");
        firebaseRecords = records;
        //recyclerViewAdapter.swap(records);
        recyclerViewAdapter = new JournalEntryRecyclerViewAdapter(firebaseRecords, mListener);
        recyclerView.swapAdapter(recyclerViewAdapter, false);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(FirebaseRecord item);
    }
}
