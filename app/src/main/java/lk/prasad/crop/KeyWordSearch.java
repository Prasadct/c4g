package lk.prasad.crop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lk.prasad.crop.common.ProfileData;


/**
 * Created by prasadt on 6/10/2016.
 */
public class KeyWordSearch extends Activity implements AdapterView.OnItemSelectedListener {

    // List view
    private ListView lv;

    // Listview Adapter
    MyAdapter adapter;

    // Search EditText
    EditText inputSearch;


    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.key_search_list);

        // Listview Data
        List<String> strings = new ArrayList<String>();
//        strings.add("Bumiya");
//        strings.add("Boga Prabeda");
//        strings.add("Beeja");
//        strings.add("Pohora");
//        strings.add("Pasa");
//        strings.add("Nelima");
//        strings.add("Jalaya");
//        strings.add("Paminili");
//        List<String> strings = new ArrayList<String>();
        strings.add("N+ush");//1 bumiya
        strings.add("fnda. m%fNao");//2 boga
        strings.add("nSc");//3 biua
        strings.add("fmdfydr");//4 pohora
        strings.add("mi");//5 pasa
        strings.add("wiajkq fk,Su");//6 aswanu nelima
        strings.add("c,h");//7 jalaya
        strings.add("b,a ueiaid");//8 illmessa
        strings.add("hkaf;%damlrK");//9 yanthropakarana
        strings.add("Y%uslhka");//10 sramikayan
        strings.add("f.djsm,");//11 govipala
        strings.add("m,sfndaO");//12 palipodha
        strings.add(".vq ueiaid");//13 gadumessa
        /*Integer[] imageId = { R.drawable.bum,
                R.drawable.prabeda,
                R.drawable.bija,
                R.drawable.pohora,
                R.drawable.pasa,
                R.drawable.nelima,
                R.drawable.jalaya,
                R.drawable.paminili};

        List<Integer> integers = new ArrayList<Integer>();
        integers.add(imageId[0]);
        integers.add(imageId[1]);
        integers.add(imageId[2]);
        integers.add(imageId[3]);
        integers.add(imageId[4]);
        integers.add(imageId[5]);
        integers.add(imageId[6]);
        integers.add(imageId[7]);*/

        lv = (ListView) findViewById(R.id.key_search_list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearch);

        // Adding items to listview
//                (TextView) View.findViewById(R.id.product_name)).setTypeface(Typeface.createFromAsset(view.getContext().getAssets(), "fonts/iskpotab.ttf"));
        adapter = new MyAdapter(this,(TextView)findViewById(R.id.product_name), strings);
//        adapter = new ArrayAdapter<String>(this, R.layout.key_search_list_item, R.id.product_name, products);
        lv.setAdapter(adapter);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                System.out.println("Text [" + cs + "] - Start [" + arg1 + "] - Before [" + arg2 + "] - Count [" + arg3 + "]");
                if (arg3 < arg2) {
                    // We're deleting char so we need to reset the adapter data
                    KeyWordSearch.this.adapter.resetData();
                }
                // When user changed the Text
                KeyWordSearch.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                String value = (String) adapter.getItemAtPosition(position);
                System.out.println("Value ---------- " + value);
                if (value.equalsIgnoreCase("N+ush")){//1
                    openResutl(1);
                } else if (value.equalsIgnoreCase("fnda. m%fNao")){//2
                    openResutl(3);
                } else if (value.equalsIgnoreCase("nSc")){//3
                    openResutl(3);
                } else if (value.equalsIgnoreCase("fmdfydr")){//4
                    openResutl(6);
                } else if (value.equalsIgnoreCase("mi")){//5
                    openResutl(2);
                } else if (value.equalsIgnoreCase("wiajkq fk,Su")){//6
                    openResutl(9);
                } else if (value.equalsIgnoreCase("c,h")){//7
                    openResutl(5);
                } else if (value.equalsIgnoreCase("b,a ueiaid")){//8
                    openResutl(7, "illmassa");
                } else if (value.equalsIgnoreCase("hkaf;%damlrK")){//9
                    openResutl(8);
                } else if (value.equalsIgnoreCase("Y%uslhka")){//10
                    openResutl(10);
                } else if (value.equalsIgnoreCase("f.djsm,")){//11
                    openResutl(8);
                } else if (value.equalsIgnoreCase("m,sfndaO")){//12
                    openResutl(7);
                } else if (value.equalsIgnoreCase(".vq ueiaid")){//13
                    openResutl(7, "gadumessa");
                }
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void openResutl(int position){
        Intent intent = new Intent(KeyWordSearch.this, WebViewActivity.class);
        intent.putExtra("url", "http://www.cropadvisor.site/details?cat="+position+"&generatedfrom=detail&id="+ ProfileData.getPhoneID((TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE)));
        startActivity(intent);
    }

    public void openResutl(int position, String dev){
        Intent intent = new Intent(KeyWordSearch.this, WebViewActivity.class);
        intent.putExtra("url", "http://www.cropadvisor.site/details?cat="+position+"#"+ dev +"&generatedfrom=detail&id="+ ProfileData.getPhoneID((TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE)));
        startActivity(intent);
    }
}