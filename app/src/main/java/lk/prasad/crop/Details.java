package lk.prasad.crop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import lk.prasad.crop.common.ProfileData;

public class Details extends Activity {
	TextView tv_main;
	String[] detailsType_si = {"භුමිය තේරීම",
            "බිම් පිළියෙළ කිරීම",
            "බෝග ප්‍රබේද හා බීජ \nභාවිතය",
            "බෝග පිහිටුවීම",
            "ජල සම්පාදනය හා ජල කළමනාකරණය",
            "පොහොර හා පසට එකතු \nකරන වෙනත් දෑ",
            "ශාක සෞඛ්‍ය හා \nආරක්ෂණය",
            "ගොවිපළ  යන්ත්‍රෝපකරණ",
            "අස්වනු නෙලීම හා  \nපරිහරණය",
            "ශ්‍රමිකයින්ගේ ආරක්ෂාව, \nසෞඛ්‍ය හා සුබසාධනය",
            "ලේඛන හා වාර්තා තබා\n ගැනීම"};
	String[] detailsType_en = {"Field Preparation","Packing","Fertilizer","Pesticide","Irrigation","Maintenance","Harvesting","Field Preparation","Packing","Fertilizer","Pesticide","Irrigation","Maintenance","Harvesting"};
	Integer[] imageId = { R.drawable.select_field,
            R.drawable.field_preperation,
            R.drawable.seed,
            R.drawable.planting,
            R.drawable.irrigation,
            R.drawable.fertilizer,
            R.drawable.health,
            R.drawable.machines,
            R.drawable.harvesting,
            R.drawable.safety,
            R.drawable.documenting};
	
	String[] category={"1","2","3","4","5","6","7","8","9","10","11"};

	ListView list;
	int language;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);

		tv_main=(TextView)findViewById(R.id.tv_main);
		
		language = DashBoard.languageId;
		MainList adapter = new MainList(Details.this,
				setMailList(language), imageId);
		list = (ListView) findViewById(R.id.listView);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(Details.this, WebViewActivity.class);
				intent.putExtra("url", getResources().getString(R.string.service_url)+"details?cat="+category[position]+"&generatedfrom=detail&id="+ ProfileData.getPhoneID((TelephonyManager)getSystemService(getApplicationContext().TELEPHONY_SERVICE)));
				startActivity(intent);

			}
		});

	}

	private String[] setMailList(int language) {

		if (language == 1) {
			return detailsType_en;
		} else if (language == 2) {
			tv_main.setText("fnda.h ms,sn| f;dr;=re");
			Typeface font = Typeface.createFromAsset(DashBoard.assetManager,
					"FM-BINDU.TTF");
			tv_main.setTypeface(font);
			return detailsType_si;
		}
		return detailsType_en;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.prof_menu){
            Intent intent = new Intent(Details.this,
                    UserProfile.class);
            startActivity(intent);
        } else if (id == R.id.key_search_menu){
			Intent intent = new Intent(this.getApplicationContext(),
					KeyWordSearch.class);
			startActivity(intent);
		}

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
}