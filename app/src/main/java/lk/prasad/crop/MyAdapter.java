package lk.prasad.crop;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasadt on 6/13/2016.
 */
public class MyAdapter extends BaseAdapter implements Filterable {

    private Filter planetFilter;
    private List<String> strings; // obviously don't use object, use whatever you really want
    private List<String> stringso;
    private final Context context;
    private TextView textView;

    public MyAdapter(Context context, TextView textView, List<String> strings) {
        this.context = context;
        this.strings = strings;
        this.textView = textView;
        this.stringso = strings;
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public Object getItem(int position) {
        return strings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String s = strings.get(position);
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(R.layout.key_search_list_item, null);
        }
        TextView tv = (TextView) v.findViewById(R.id.product_name);
        tv.setText(s); // use whatever method you want for the label
        tv.setTypeface(Typeface.createFromAsset(DashBoard.assetManager,
                "FM-BINDU.TTF"));
        // set whatever typeface you want here as well
//        ImageView tv = (ImageView) v.findViewById(R.id.product_name);

        return v;
    }

    @Override
    public Filter getFilter() {
        if (planetFilter == null)
            planetFilter = new PlanetFilter();

        return planetFilter;
    }

    private class PlanetFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults oReturn = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                oReturn.values = stringso;
            } else {
                // We perform filtering operation
                List<String> nPlanetList = new ArrayList<String>();

//                    for (String p : stringso) {
//                        if (p.toUpperCase().startsWith(constraint.toString().toUpperCase()))
//                            nPlanetList.add(p);
//                    }
                if (constraint.toString().equalsIgnoreCase("b")) {
                    nPlanetList.add(stringso.get(0));
                    nPlanetList.add(stringso.get(1));
                    nPlanetList.add(stringso.get(2));
                } else if (constraint.toString().equalsIgnoreCase("bu")) {
                    nPlanetList.add(stringso.get(0));
                } else if (constraint.toString().equalsIgnoreCase("bum")) {
                    nPlanetList.add(stringso.get(0));
                } else if (constraint.toString().equalsIgnoreCase("bumi")) {
                    nPlanetList.add(stringso.get(0));
                } else if (constraint.toString().equalsIgnoreCase("bumiy")) {
                    nPlanetList.add(stringso.get(0));
                } else if (constraint.toString().equalsIgnoreCase("bo")) {
                    nPlanetList.add(stringso.get(1));
                } else if (constraint.toString().equalsIgnoreCase("bog")) {
                    nPlanetList.add(stringso.get(1));
                } else if (constraint.toString().equalsIgnoreCase("boga")) {
                    nPlanetList.add(stringso.get(1));
                } else if (constraint.toString().equalsIgnoreCase("bi")) {
                    nPlanetList.add(stringso.get(2));
                }  else if (constraint.toString().equalsIgnoreCase("be")) {
                    nPlanetList.add(stringso.get(2));
                } else if (constraint.toString().equalsIgnoreCase("p")) {
                    nPlanetList.add(stringso.get(3));
                    nPlanetList.add(stringso.get(4));
                    nPlanetList.add(stringso.get(11));
                } else if (constraint.toString().equalsIgnoreCase("pa")) {
                    nPlanetList.add(stringso.get(4));
                    nPlanetList.add(stringso.get(11));
                } else if (constraint.toString().equalsIgnoreCase("pal")) {
                    nPlanetList.add(stringso.get(11));
                } else if (constraint.toString().equalsIgnoreCase("pas")) {
                    nPlanetList.add(stringso.get(4));
                } else if (constraint.toString().equalsIgnoreCase("po")) {
                    nPlanetList.add(stringso.get(3));
                } else if (constraint.toString().equalsIgnoreCase("a")) {
                    nPlanetList.add(stringso.get(5));
                } else if (constraint.toString().equalsIgnoreCase("as")) {
                    nPlanetList.add(stringso.get(5));
                } else if (constraint.toString().equalsIgnoreCase("j")) {
                    nPlanetList.add(stringso.get(6));
                } else if (constraint.toString().equalsIgnoreCase("ja")) {
                    nPlanetList.add(stringso.get(6));
                } else if (constraint.toString().equalsIgnoreCase("jal")) {
                    nPlanetList.add(stringso.get(6));
                } else if (constraint.toString().equalsIgnoreCase("jala")) {
                    nPlanetList.add(stringso.get(6));
                } else if (constraint.toString().equalsIgnoreCase("i")) {
                    nPlanetList.add(stringso.get(7));
                } else if (constraint.toString().equalsIgnoreCase("il")) {
                    nPlanetList.add(stringso.get(7));
                } else if (constraint.toString().equalsIgnoreCase("y")) {
                    nPlanetList.add(stringso.get(8));
                } else if (constraint.toString().equalsIgnoreCase("ya")) {
                    nPlanetList.add(stringso.get(8));
                } else if (constraint.toString().equalsIgnoreCase("yan")) {
                    nPlanetList.add(stringso.get(8));
                } else if (constraint.toString().equalsIgnoreCase("s")) {
                    nPlanetList.add(stringso.get(9));
                } else if (constraint.toString().equalsIgnoreCase("sr")) {
                    nPlanetList.add(stringso.get(9));
                } else if (constraint.toString().equalsIgnoreCase("sra")) {
                    nPlanetList.add(stringso.get(9));
                } else if (constraint.toString().equalsIgnoreCase("sram")) {
                    nPlanetList.add(stringso.get(9));
                } else if (constraint.toString().equalsIgnoreCase("g")) {
                    nPlanetList.add(stringso.get(10));
                    nPlanetList.add(stringso.get(12));
                } else if (constraint.toString().equalsIgnoreCase("ga")) {
                    nPlanetList.add(stringso.get(12));
                } else if (constraint.toString().equalsIgnoreCase("gad")) {
                    nPlanetList.add(stringso.get(12));
                } else if (constraint.toString().equalsIgnoreCase("go")) {
                    nPlanetList.add(stringso.get(10));
                } else if (constraint.toString().equalsIgnoreCase("gov")) {
                    nPlanetList.add(stringso.get(10));
                } else if (constraint.toString().equalsIgnoreCase("govi")) {
                    nPlanetList.add(stringso.get(10));
                }

                oReturn.values = nPlanetList;

            }
//                oReturn.values = strings;
//                if (orig == null)
//                    orig = items;
//                if (constraint != null) {
//                    if (orig != null && orig.size() > 0) {
//                        for (final station g : orig) {
//                            if (g.getName().toLowerCase()
//                                    .contains(constraint.toString()))
//                                results.add(g);
//                        }
//                    }
//                    oReturn.values = results;
//                }
            return oReturn;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults oReturn) {

            // Now we have to inform the adapter about the new list filtered
//            if (oReturn.count == 0)
//                notifyDataSetInvalidated();
//            else {
                strings = (List<String>) oReturn.values;
                notifyDataSetChanged();
//            }

        }
    }



    public void resetData() {
        strings = stringso;
    }
}