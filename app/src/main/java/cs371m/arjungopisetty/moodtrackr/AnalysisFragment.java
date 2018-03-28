package cs371m.arjungopisetty.moodtrackr;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnalysisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalysisFragment extends Fragment {

    private EditText mInputText;
    private Button mAnalyzeButton;

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
        View v = getView();
        mInputText = (EditText) v.findViewById(R.id.inputText);
        mAnalyzeButton = (Button) v.findViewById(R.id.analyzeButton);
        mAnalyzeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WatsonAnalyzer analyzer = new WatsonAnalyzer();
                String input = mInputText.getText().toString();
                Log.d(MainActivity.TAG, "Tone input: " + input);
                analyzer.analyzeText(input);
            }
        });
    }
}
