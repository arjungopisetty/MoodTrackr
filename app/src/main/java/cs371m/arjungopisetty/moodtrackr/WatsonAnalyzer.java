package cs371m.arjungopisetty.moodtrackr;

import android.util.JsonReader;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneInput;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by arjungopisetty on 3/27/18.
 */

public class WatsonAnalyzer {

    private final String apiVersion = "2017-09-21";
    private final String username = "625febec-1179-44c4-ae7a-d8672ffdbb48";
    private final String password = "qSP8fESIvAkO";

    private Map headers;

    public WatsonAnalyzer() {
        headers = new HashMap<String, String>();
        headers.put("X-Watson-Learning-Opt-Out", "true");
    }

    public void analyzeText(String inputText) {
        ToneAnalyzer service = new ToneAnalyzer(apiVersion, username, password);
        service.setDefaultHeaders(headers);

        ToneInput toneInput = new ToneInput.Builder()
                .text(inputText).build();
        ToneOptions options = new ToneOptions.Builder()
                .toneInput(toneInput).build();
        ServiceCall call = service.tone(options);
        call.enqueue(new ServiceCallback<ToneAnalysis>() {

            @Override public void onResponse(ToneAnalysis tone) {
                System.out.println(tone);
            }

            @Override public void onFailure(Exception e) {
                Log.e(MainActivity.TAG, "Error " + e.getMessage());
            }
        });

    }
}
