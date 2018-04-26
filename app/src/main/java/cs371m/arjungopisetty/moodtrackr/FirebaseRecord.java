package cs371m.arjungopisetty.moodtrackr;

import java.io.Serializable;
import java.util.List;

/**
 * Created by arjungopisetty on 3/30/18.
 */

public class FirebaseRecord {
    public String journalEntry;
    public List<ToneRecord> tones;
    public Long time;

    public FirebaseRecord() {}

    @Override
    public String toString() {
        return "TimedToneRecord{" +
                "tones=" + tones +
                ", time=" + time +
                '}';
    }
}
