package cs371m.arjungopisetty.moodtrackr;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphFragment extends Fragment implements ToneParser.FetchFirebaseCallback {

    private FirebaseReader mReader;

    private View mRootView;
    private Button mFetchButton;

    private PieChart mChart;

    public GraphFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static GraphFragment newInstance() {
        GraphFragment fragment = new GraphFragment();
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
        View v = inflater.inflate(R.layout.fragment_graph, container, false);
        mRootView = v;
        return v;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFetchButton = (Button) mRootView.findViewById(R.id.fetchButton);

        mChart = (PieChart) mRootView.findViewById(R.id.pieChart);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setCenterText("Moods");
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);
        mChart.setDrawCenterText(true);

        mReader = new FirebaseReader(this);

        mFetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mReader.fetchFromFirebase();
            }
        });
    }

    @Override
    public void onComplete(List<TimedToneRecord> records) {
        // TODO: Put into graph
        for (TimedToneRecord record : records)
            Log.d(MainActivity.TAG, record.toString());

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        for (int i = 0; i < records.size(); i++) {
            for (int j = 0; j < records.get(i).tones.size(); j++) {
                entries.add(new PieEntry((float) ((Math.random() * 10) + 10 / 5),
                        records.get(i % records.size()).tones.get(j)));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Tone Results");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);
        mChart.setData(data);
        mChart.invalidate();
    }
}
