package cs371m.arjungopisetty.moodtrackr;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class JournalActivity extends AppCompatActivity {

    public static final String TAG = JournalActivity.class.getSimpleName();

    private String journalEntry;
    private List<ToneRecord> tones;
    private Long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        FirebaseRecord record = (FirebaseRecord) getIntent().getExtras().getSerializable("record");
        journalEntry = getIntent().getStringExtra("journalEntry");
        time = getIntent().getLongExtra("time", 0);
        tones = (ArrayList<ToneRecord>) getIntent().getSerializableExtra("tones");
        Log.d(TAG, "Time: " + time);
        Log.d(TAG, "Journal record: " + journalEntry);
    }

}
