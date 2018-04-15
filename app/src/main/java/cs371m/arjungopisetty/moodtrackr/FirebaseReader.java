package cs371m.arjungopisetty.moodtrackr;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirebaseReader {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;

    //private HashMap<String, Double> countsForGraph;
    //private List<String> listOfJournalEntries;

    private ToneParser.FetchFirebaseCallback mCallback;

    public FirebaseReader(ToneParser.FetchFirebaseCallback callback) {
        mCallback = callback;

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference();
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
                    FirebaseRecord firebaseRecord = snap.getValue(FirebaseRecord.class);
                    //Log.d(MainActivity.TAG, firebaseRecord.toString());
                    //List<ToneRecord> tones = firebaseRecord.tones;
                    list.add(firebaseRecord);
                }
                mCallback.onComplete(list);
                //convertToStructures(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(MainActivity.TAG, "Query cancelled");
            }
        });
    }
}
