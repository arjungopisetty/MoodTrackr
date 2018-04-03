package cs371m.arjungopisetty.moodtrackr;

import android.os.Debug;
import android.util.Log;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.DocumentAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneCategory;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;

import org.json.JSONObject;
import org.w3c.dom.Document;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by arjungopisetty on 3/28/18.
 */

public class ToneParser {

    public interface FetchTonesCallback {
        void onComplete(List<ToneRecord> tones);
    }

    public interface FetchFirebaseCallback {
        void onComplete(HashMap<String, Double> graphData);
    }

    public static List<ToneRecord> analysisToToneRecords(ToneAnalysis analysis) {
        DocumentAnalysis documentAnalysis = analysis.getDocumentTone();
        //List<ToneCategory> categories = documentAnalysis.getToneCategories();
        List<ToneScore> scores = documentAnalysis.getTones();
        ArrayList<ToneRecord> parsedResults = new ArrayList<>();

//        if (categories == null) { Log.d(MainActivity.TAG, "categories is null"); }
//        else if (categories.size() == 0) { Log.d(MainActivity.TAG, "categories is empty"); }
//        else {
//            for (int i = 0; i < categories.size(); i++) {
//                ToneCategory toneCategory = categories.get(i);
//                System.out.println("Tone Category: " + toneCategory);
//            }
//        }

        if (scores == null) { Log.e(MainActivity.TAG, "scores are null"); }
        else if (scores.size() == 0) {
            Log.d(MainActivity.TAG, "scores are empty");
            return new ArrayList<>();
        }
        else {
            for (int i = 0; i < scores.size(); i++) {
                ToneScore score = scores.get(i);
                //Log.d(MainActivity.TAG, "Score: " + score);
                ToneRecord record = new ToneRecord();
                record.tone_name = score.getToneName();
                record.tone_id = score.getToneId();
                record.score = score.getScore();
                parsedResults.add(record);
            }
        }
        return parsedResults;
    }
}
