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
import java.util.List;

public class FirebaseReader {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;

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
                List<TimedToneRecord> list = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    TimedToneRecord timedToneRecord = snap.getValue(TimedToneRecord.class);
                    list.add(timedToneRecord);
                }
                mCallback.onComplete(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(MainActivity.TAG, "Query cancelled");
            }
        });
    }
}
