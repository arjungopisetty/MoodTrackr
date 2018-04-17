package cs371m.arjungopisetty.moodtrackr;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import cs371m.arjungopisetty.moodtrackr.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements JournalFragment.OnListFragmentInteractionListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private View parentLayout;

    private AnalysisFragment analysisFragment;
    private GraphFragment graphFragment;
    private JournalFragment journalFragment;

    private DateFormat formatter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchToAnalysisFragment();
                    //mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    // TODO: Graph fragment
                    switchToGraphFragment();
                    return true;
                case R.id.navigation_journals:
                    // TODO: Journals fragment
                    switchToJournalFragment();
                    return true;
                case R.id.navigation_notifications:
                    // TODO: Insights fragment

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        parentLayout = findViewById(android.R.id.content);

        analysisFragment = AnalysisFragment.newInstance();
        graphFragment = GraphFragment.newInstance();
        journalFragment = JournalFragment.newInstance();

        formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        switchToAnalysisFragment();
    }

    private void switchToAnalysisFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, analysisFragment);
        ft.commit();
    }

    private void switchToGraphFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, graphFragment);
        ft.commit();
    }

    private void switchToJournalFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, journalFragment);
        ft.commit();
    }

    @Override
    public void onListFragmentInteraction(FirebaseRecord item) {
        String text = formatter.format(new Date(item.time));
        Snackbar.make(parentLayout, text, Snackbar.LENGTH_SHORT).show();
    }
}
