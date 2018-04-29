package cs371m.arjungopisetty.moodtrackr;

import java.util.HashMap;

public class MoodColors {

    protected static HashMap<String, Integer> colorMap;

    static {
        colorMap = new HashMap<>();
        colorMap.put("anger", R.drawable.red_circle);
        colorMap.put("fear", R.drawable.orange_circle);
        colorMap.put("joy", R.drawable.yellow_circle);
        colorMap.put("sadness", R.drawable.green_circle);
        colorMap.put("analytical", R.drawable.blue_circle);
        colorMap.put("confident", R.drawable.indigo_circle);
        colorMap.put("tentative", R.drawable.violet_circle);
    }
}
