package com.example.crop;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Details extends Activity {

	String[] detailsType_si = {"ìï ieliqu","weiSÍu","fmdfydr","lDñkdYl","c, iïmdokh","kv;a;=j","wiajekak fk,Su"};
	String[] detailsType_en = {"Field Preparation","Packing","Fertilizer","Pesticide","Irrigation","Maintenance","Harvesting"};
	Integer[] imageId = { R.drawable.lst, R.drawable.sd, R.drawable.rpt,R.drawable.support};
	
	
	ListView list;
	int language;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);

		
		
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
				startActivity(intent);

			}
		});

	}

	private String[] setMailList(int language) {

		if (language == 1) {
			return detailsType_en;
		} else if (language == 2) {
			return detailsType_si;
		}
		return detailsType_en;
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