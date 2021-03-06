package cs371m.arjungopisetty.moodtrackr;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements JournalFragment.OnListFragmentInteractionListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private AnalysisFragment analysisFragment;
    private GraphFragment graphFragment;
    private JournalFragment journalFragment;
    private SettingsFragment settingsFragment;

    private FirebaseAuth firebaseAuth;

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
                    // TODO: Settings fragment
                    switchToSettingsFragment();
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

        analysisFragment = AnalysisFragment.newInstance();
        graphFragment = GraphFragment.newInstance();
        journalFragment = JournalFragment.newInstance();
        settingsFragment = SettingsFragment.newInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        switchToAnalysisFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logoutOfFirebase();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutOfFirebase() {
        firebaseAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
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

    private void switchToSettingsFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, settingsFragment);
        ft.commit();
    }

    @Override
    public void onListFragmentInteraction(FirebaseRecord item) {
//        String text = formatter.format(new Date(item.time));
//        Snackbar.make(parentLayout, text, Snackbar.LENGTH_SHORT).show();
        Intent intent = new Intent(this, JournalActivity.class);
        intent.putExtra("journalEntry", item.journalEntry);
        intent.putExtra("time", item.time);
        intent.putExtra("tones", (ArrayList<ToneRecord>) item.tones);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "Reached onActivityResult");
        switchToJournalFragment();
    }
}
