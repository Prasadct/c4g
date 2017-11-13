package lk.prasad.crop;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by prasadt on 6/13/2016.
 */
public class ListAdapter extends ArrayAdapter<ClipData.Item> {

    public ListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListAdapter(Context context, int resource, List<ClipData.Item> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

//        if (v == null) {
//            LayoutInflater vi;
//            vi = LayoutInflater.from(getContext());
//            v = vi.inflate(R.layout.key_search_list_item, null);
//        }
//
//        ClipData.Item p = getItem(position);
//
//        if (p != null) {
//            TextView tt1 = (TextView) v.findViewById(R.id.id);
//
//            if (tt1 != null) {
//                tt1.setText(p.getText());
//            }
//        }

        return v;
    }

}