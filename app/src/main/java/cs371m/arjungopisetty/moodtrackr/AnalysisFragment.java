package cs371m.arjungopisetty.moodtrackr;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnalysisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalysisFragment extends Fragment implements ToneParser.FetchTonesCallback {

    private EditText mInputText;
    private Button mAnalyzeButton;

    private WatsonAnalyzer analyzer;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;

    private String[] dummydata = {"1", "2", "3", "4"};


    public AnalysisFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AnalysisFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnalysisFragment newInstance() {
        AnalysisFragment fragment = new AnalysisFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_analysis, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        analyzer = new WatsonAnalyzer(this);
        View v = getView();
        mInputText = (EditText) v.findViewById(R.id.inputText);
        mAnalyzeButton = (Button) v.findViewById(R.id.analyzeButton);
        mAnalyzeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = mInputText.getText().toString();
                Log.d(MainActivity.TAG, "Tone input: " + input);
                analyzer.analyzeText(input);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onComplete(final List<ToneRecord> tones) {
        // Create and display custom dialog
//        for (ToneRecord t : tones)
//            Log.d(MainActivity.TAG, "ID: " + t.tone_id + " Score: " + t.score);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Tones");
        View innerView = (View) getLayoutInflater().inflate(R.layout.custom_dialog_layout, null);
        builder.setView(innerView);
        ListView tonesListView = (ListView) innerView.findViewById(R.id.customListView);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, dummydata);
        //TextView text = innerView.findViewById(R.id.dial)
        //tonesListView.setAdapter(adapter);
        DialogCustomListViewAdapter adapter = new DialogCustomListViewAdapter(getActivity());
        adapter.addAll(tones);
        tonesListView.setAdapter(adapter);
        builder.setPositiveButton("Push", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //TODO: Push to firebase here
                pushToFirebase(tones);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog dialogAlert = builder.create();
                dialogAlert.show();
            }
        });
    }

    private void pushToFirebase(List<ToneRecord> tones) {
        TimedToneRecord pushRecord = new TimedToneRecord();
        pushRecord.tones = tones;
        pushRecord.time = new Long(System.currentTimeMillis());

        mDatabase.child("users").child(mUser.getUid()).push().setValue(pushRecord);

        Toast.makeText(getContext(), "Pushed to Firebase DB", Toast.LENGTH_SHORT).show();
    }
}
