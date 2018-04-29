package cs371m.arjungopisetty.moodtrackr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ClearFirebaseActivity extends AppCompatActivity implements ToneParser.ClearFirebaseCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_clearfirebase);

        FirebaseReader reader = new FirebaseReader(this);
        reader.clearFirebaseRecords();
    }

    @Override
    public void onClearComplete() {
        Toast.makeText(this, "Firebase DB cleared", Toast.LENGTH_SHORT).show();
        finish();
    }
}
