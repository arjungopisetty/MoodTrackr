package cs371m.arjungopisetty.moodtrackr;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;

/**
 * Created by arjungopisetty on 3/28/18.
 */

public class ToneRecord {
    public String tone_name;
    public String tone_id;
    public double score;

    public ToneRecord() {}
//
//    public ToneRecord(String tone_name, String tone_id, double score) {
//        this.tone_name = tone_name;
//        this.tone_id = tone_id;
//        this.score = score;
//    }


    @Override
    public String toString() {
        return "ToneRecord{" +
                "tone_name='" + tone_name + '\'' +
                ", tone_id='" + tone_id + '\'' +
                ", score=" + score +
                '}';
    }
}
