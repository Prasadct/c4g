package com.example.crop;

import com.example.crop.R;
import com.example.crop.R.array;
import com.example.crop.R.drawable;
import com.example.crop.R.id;
import com.example.crop.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	ListView list;
	int language = 1;
	String[] mainList;

	private String[] setMailList(int language) {

		Resources resources = getResources();
		String[] mainListEnglish = resources
				.getStringArray(R.array.main_list_items_EN);
		String[] mainListSinhala = resources
				.getStringArray(R.array.main_list_items_SI);
		String[] getMainListTamil = resources
				.getStringArray(R.array.main_list_items_TA);

		if (language == 1) {
			return mainListEnglish;
		} else if (language == 2) {
			return mainListSinhala;
		} else if (language == 3) {
			return getMainListTamil;
		}
		return mainListEnglish;
	}

	Integer[] imageId = { R.drawable.lst, R.drawable.sd, R.drawable.rpt,
			R.drawable.support

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final Context context = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		language = DashBoard.languageId;
		MainList adapter = new MainList(MainActivity.this,
				setMailList(language), imageId);
		list = (ListView) findViewById(R.id.listView);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (position == 0) {
					Intent intent = new Intent(MainActivity.this, Details.class);
					startActivity(intent);
				} else if (position == 1) {
//					Intent intent = new Intent(MainActivity.this,
//							WebViewActivity.class);
//					intent.putExtra("url", "http://128.199.125.48/Project/Disease.php?cropId=1&id="+DashBoard.cropId);
//					startActivity(intent);
					
					Intent intent = new Intent(MainActivity.this,
							Disease.class);
					startActivity(intent);
				} else if (position == 2) {
					Intent intent = new Intent(MainActivity.this,
							ReportDedeases.class);
					startActivity(intent);
				}
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		// noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
