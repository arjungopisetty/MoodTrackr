package cs371m.arjungopisetty.moodtrackr;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class JournalActivity extends AppCompatActivity {

    public static final String TAG = JournalActivity.class.getSimpleName();

    protected ListView tonesListView;
    protected TextView journalTextView;

    private String journalEntry;
    private List<ToneRecord> tones;
    private Long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tonesListView = findViewById(R.id.journalMoodListView);
        journalTextView = findViewById(R.id.journalTextField);

        journalTextView.setMovementMethod(new ScrollingMovementMethod());

//        FirebaseRecord record = (FirebaseRecord) getIntent().getExtras().getSerializable("record");
        journalEntry = getIntent().getStringExtra("journalEntry");
        time = getIntent().getLongExtra("time", 0);
        tones = (ArrayList<ToneRecord>) getIntent().getSerializableExtra("tones");
        Log.d(TAG, "Time: " + time);
        Log.d(TAG, "Journal record: " + journalEntry);
        Log.d(TAG, "Tones" + tones);

        DialogCustomListViewAdapter adapter = new DialogCustomListViewAdapter(getApplicationContext());
        adapter.addAll(tones);
        tonesListView.setAdapter(adapter);

        journalTextView.setText(journalEntry);
    }


}
