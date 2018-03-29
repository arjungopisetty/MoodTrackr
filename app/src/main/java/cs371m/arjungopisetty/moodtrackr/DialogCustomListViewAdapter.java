package cs371m.arjungopisetty.moodtrackr;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by arjungopisetty on 3/29/18.
 */

public class DialogCustomListViewAdapter extends ArrayAdapter<ToneRecord> {

    static class ViewHolder {
        TextView theTextView;
        ImageView theImageView;

        public ViewHolder(View v) {
            theTextView = (TextView) v.findViewById(R.id.dialogRowText);
            theImageView = (ImageView) v.findViewById(R.id.dialogRowPic);
        }
    }

    private LayoutInflater theInflater = null;

    public DialogCustomListViewAdapter(@NonNull Context context) {
        super(context, R.layout.pic_text_row);
        theInflater = LayoutInflater.from(getContext());
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = theInflater.inflate(R.layout.pic_text_row, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        ToneRecord record = getItem(position);
        vh.theTextView.setText("1");
        vh.theImageView.setImageResource(R.drawable.blue_circle);
        return convertView;
    }
}
