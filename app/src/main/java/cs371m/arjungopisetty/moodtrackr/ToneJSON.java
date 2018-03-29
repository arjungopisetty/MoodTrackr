package cs371m.arjungopisetty.moodtrackr;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arjungopisetty on 3/28/18.
 */

public class ToneJSON {

    public interface FetchCallback {
        void onComplete(List<ToneRecord> tones);
    }

    public static List<ToneRecord> jsonToStringRecords(ToneAnalysis analysis) {
        return new ArrayList<>();
    }
}
