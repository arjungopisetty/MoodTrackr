package cs371m.arjungopisetty.moodtrackr;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphFragment extends Fragment implements ToneParser.FetchFirebaseCallback,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private FirebaseReader mReader;

    private View mRootView;
    private Button mFetchButton, mDatePickerButton, mTimePickerButton;

    private PieChart mChart;

    private ArrayList<Integer> colors;

    private HashMap<String, Double> countsForGraph;
    private List<String> listOfJournalEntries;

    public GraphFragment() {
        // Required empty public constructor
    }

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
        mDatePickerButton = (Button) mRootView.findViewById(R.id.datePicker);
        mTimePickerButton = (Button) mRootView.findViewById(R.id.timePicker);

        mChart = (PieChart) mRootView.findViewById(R.id.pieChart);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setCenterText("Moods");
        mChart.setDrawHoleEnabled(true);
        mChart.setEntryLabelColor(Color.BLACK);
        mChart.setHoleColor(Color.WHITE);
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);
        mChart.setDrawCenterText(true);
        mChart.setDrawEntryLabels(false);

        mReader = new FirebaseReader(this);

        mFetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mReader.fetchFromFirebase();
            }
        });

        // DatePickerFragment
        Calendar now = Calendar.getInstance();
        final DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        mDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpd.show(getChildFragmentManager(), "Datepickerdialog");
            }
        });
        // TimePickerFragment
        final TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        mTimePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tpd.show(getChildFragmentManager(), "Timepickerdialog");
            }
        });

        // Add colors for graph
        colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());

        countsForGraph = new HashMap<>();
    }

    public void convertToStructures(List<FirebaseRecord> records) {
        for (int i = 0; i < records.size(); i++) {
            List<ToneRecord> listOfTones = records.get(i).tones;
            //listOfJournalEntries.add(records.get(i).journalEntry);
            for (int j = 0; j < listOfTones.size(); j++) {
                ToneRecord toneRecord = listOfTones.get(j);
                if (countsForGraph.get(toneRecord.tone_id) != null) {
                    //Log.d(MainActivity.TAG, "Duplicate key");
                    countsForGraph.put(toneRecord.tone_id,
                            countsForGraph.get(toneRecord.tone_id) + toneRecord.score);
                } else {
                    //Log.d(MainActivity.TAG, "First key " + toneRecord.tone_id);
                    countsForGraph.put(toneRecord.tone_id, toneRecord.score);
                }
            }
        }

    }

    @Override
    public void onComplete(List<FirebaseRecord> records) {
        convertToStructures(records);

        DecimalFormat decimalFormat = new DecimalFormat("#");
        List<PieEntry> entries = new ArrayList<>();
        Iterator iterator = countsForGraph.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            Log.d(MainActivity.TAG, pair.getKey() + " = " + pair.getValue());
            entries.add(new PieEntry(Float.parseFloat(decimalFormat.format(pair.getValue())), (String) pair.getKey()));
            iterator.remove();
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mChart.setData(data);
        mChart.invalidate();
    }

    /**
     * @param view           The view associated with this listener.
     * @param year           The year that was set.
     * @param monthOfYear    The month that was set (0-11) for compatibility
     *                       with {@link Calendar}.
     * @param dayOfMonth     The day of the month that was set.
     * @param yearEnd
     * @param monthOfYearEnd
     * @param dayOfMonthEnd
     */
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        Log.d(MainActivity.TAG, "Reached onDateSet");
        String startDate = dayOfMonth + "/" + (++monthOfYear) + "/" + year;
        String endDate = dayOfMonthEnd + "/" + (++monthOfYearEnd) + "/" + yearEnd;
        String date = "You picked the following date: From- " + dayOfMonth + "/" + (++monthOfYear) + "/" + year + " To " + dayOfMonthEnd + "/" + (++monthOfYearEnd) + "/" + yearEnd;
        Log.d(MainActivity.TAG, date);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date parsedStartDate = sdf.parse(startDate);
            long startMillis = parsedStartDate.getTime();
            Log.d("TAG", startMillis + "");

            Date parsedEndDate = sdf.parse(endDate);
            long endMillis = parsedEndDate.getTime();
            Log.d("TAG", endMillis + "");

            mReader.fetchFromFirebase(startMillis, endMillis);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param view         The view associated with this listener.
     * @param hourOfDay    The hour that was set.
     * @param minute       The minute that was set.
     * @param hourOfDayEnd
     * @param minuteEnd
     */
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {

    }
}
