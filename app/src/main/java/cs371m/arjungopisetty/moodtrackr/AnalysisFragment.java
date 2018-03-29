package cs371m.arjungopisetty.moodtrackr;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnalysisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalysisFragment extends Fragment implements ToneJSON.FetchCallback {

    private EditText mInputText;
    private Button mAnalyzeButton;

    private WatsonAnalyzer analyzer;

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
    }

    @Override
    public void onComplete(List<ToneRecord> tones) {
        // TODO: custom dialog here
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Tones:");
                View innerView = (View) getLayoutInflater().inflate(R.layout.custom_dialog_layout, null);
                alertDialog.setView(innerView);
                ListView tonesListView = (ListView) innerView.findViewById(R.id.customListView);
                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, dummydata);
                //TextView text = innerView.findViewById(R.id.dial)
                //tonesListView.setAdapter(adapter);
                DialogCustomListViewAdapter adapter = new DialogCustomListViewAdapter(getActivity());
                adapter.add(new ToneRecord());
                adapter.add(new ToneRecord());
                adapter.add(new ToneRecord());
                tonesListView.setAdapter(adapter);
                alertDialog.show();
            }
        });
    }
}
