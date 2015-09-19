package com.example.crop;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class DashBoard extends Activity implements OnItemSelectedListener {

	boolean ableToExit=false;
	
	private GridView gridView;
    private GridViewAdapter gridAdapter;

    TextView tv_type;
    
	public static ArrayList<String> arrayFiles = new ArrayList<String>();

	public static File  directory;

	
	String [] types={"Fruit","Vegetable"};
	int type=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);

		tv_type=(TextView)findViewById(R.id.tv_type);

		gridView = (GridView) findViewById(R.id.gridView);
		
		if (getLanguage() == 2) {

			tv_type.setText("Tnf.a j.d ldKavh f;darkak");
			Typeface font = Typeface.createFromAsset(getAssets(), "FM-BINDU.TTF");
			tv_type.setTypeface(font);
			
		} 

		loadCropTypes();

		ContextWrapper cw = new ContextWrapper(getApplicationContext());

		directory= cw.getDir("funnyVideo", Context.MODE_APPEND);



		  File f = new File(directory.toString());
	        f.mkdirs();
	        File[] file = f.listFiles();

	        if (file!=null && file.length != 0){

	               for (int i=0; i<file.length; i++)
	                   arrayFiles.add(file[i].getName());
	        }


	        
	        
	        
	     // Spinner element
			Spinner spinner = (Spinner) findViewById(R.id.spinner1);

			// Spinner click listener
			spinner.setOnItemSelectedListener(this);

			// Creating adapter for spinner
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, types);
			// attaching data adapter to spinner
			spinner.setAdapter(dataAdapter);
			

			// Drop down layout style - list view with radio button
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
	}






	public void loadCropTypes(){


		 gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, SplashScreen.VIDEO_TYPE_LIST,SplashScreen.VIDEO_TYPE_IMG_URL_LIST);
	     gridView.setAdapter(gridAdapter);

		// ListView Item Click Listener
	     gridView.setOnItemClickListener(new OnItemClickListener() {

			 @Override
			 public void onItemClick(AdapterView<?> parent, View view,
									 int position, long id) {

				 ableToExit = false;
				 int itemValue = SplashScreen.VIDEO_TYPE_ID_LIST.get(position);

				 Intent i = new Intent(DashBoard.this, DashBoard.class);
				 i.putExtra("cropid", itemValue);
				 startActivity(i);
			}

		});
	}






	protected boolean isAppInstalled(String packageName) {
        Intent mIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            return true;
        }
        else {
            return false;
        }
    }




	// exit for back button
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if(ableToExit){
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				DashBoard.this.startActivity(intent);

				finish();
				System.exit(0);

			}
			else{
				ableToExit=true;
				Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_LONG).show();
			}


			return true;
		}
		return super.onKeyDown(keyCode, event);
	}



	private int getLanguage() {
		Cursor cursor = SplashScreen.DB
				.rawQuery("SELECT * FROM language", null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				return cursor.getInt(0);
			}
		}
		cursor.close();
		return 0;
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		type=(position+1);
	}






	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

}


