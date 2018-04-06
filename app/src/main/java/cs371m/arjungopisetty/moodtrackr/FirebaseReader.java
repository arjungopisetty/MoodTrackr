package cs371m.arjungopisetty.moodtrackr;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class FirebaseReader {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;

    private HashMap<String, Double> countsForGraph;

    private ToneParser.FetchFirebaseCallback mCallback;

    public FirebaseReader(ToneParser.FetchFirebaseCallback callback) {
        mCallback = callback;

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        countsForGraph = new HashMap<>();
    }

    public void fetchFromFirebase() {
        Query query = mDatabase.child("users").
                child(mUser.getUid()).
                orderByChild("time");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<FirebaseRecord> list = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    FirebaseRecord timedToneRecord = snap.getValue(FirebaseRecord.class);
                    //Log.d(MainActivity.TAG, timedToneRecord.toString());
                    //List<ToneRecord> tones = timedToneRecord.tones;
                    list.add(timedToneRecord);
                }
                convertToToneCategories(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(MainActivity.TAG, "Query cancelled");
            }
        });
    }

    public void convertToToneCategories(List<FirebaseRecord> records) {
        for (int i = 0; i < records.size(); i++) {
            List<ToneRecord> listOfTones = records.get(i).tones;
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
        mCallback.onComplete(countsForGraph);
    }
}
