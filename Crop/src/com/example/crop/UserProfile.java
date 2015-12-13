package com.example.crop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UserProfile extends Activity implements OnItemSelectedListener {

	List<String> province = new ArrayList<String>();
	String[] province_en = { "aa", "ddd", "sssd", "ss" };
	String[] province_si = { "aa", "ddd", "sssd", "ss" };

	TextView tv_profile, tv_name, tv_phone, tv_province, tv_email;
	
	EditText et_name,et_tel,et_email;
	
	String name,tel,prov,email;

	ImageButton btn_add;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile);

		tv_profile = (TextView) findViewById(R.id.tv_profile);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_province = (TextView) findViewById(R.id.tv_province);
		tv_email = (TextView) findViewById(R.id.tv_email);
		
		
		et_name = (EditText) findViewById(R.id.et_name);
		et_tel = (EditText) findViewById(R.id.et_tel);
		et_email = (EditText) findViewById(R.id.et_email);
		
		
		btn_add=(ImageButton)findViewById(R.id.btn_add);
		
		if (getLanguage() == 2) {
			province = Arrays.asList(province_si);
			btn_add.setBackgroundResource(R.drawable.add_si);
			
			tv_profile.setText("Tnf.a �ia;r");
			tv_name.setText("ku");
			tv_phone.setText("�rl:k wxlh");
			tv_province.setText("m<d;");

			Typeface font = Typeface.createFromAsset(getAssets(), "FM-BINDU.TTF");
			tv_profile.setTypeface(font);
			tv_name.setTypeface(font);
			tv_phone.setTypeface(font);
			tv_province.setTypeface(font);
			

		} else {
			province = Arrays.asList(province_en);
			btn_add.setBackgroundResource(R.drawable.add_si);
		}


		
		// Spinner element
		Spinner spinner = (Spinner) findViewById(R.id.spinner2);

		// Spinner click listener
		spinner.setOnItemSelectedListener(this);

		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, province);
		// attaching data adapter to spinner
		spinner.setAdapter(dataAdapter);
		

		// Drop down layout style - list view with radio button
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		

		
		btn_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				name=et_name.getText().toString();
				tel=et_tel.getText().toString();
				email=et_email.getText().toString();
				
				ContentValues cv = new ContentValues();
				cv.put("name", name);
				cv.put("tel", tel);
				cv.put("province", prov);
				cv.put("email", email);
				SplashScreen.DB.insert("profile", null, cv);
				
				Toast.makeText(getApplicationContext(), "User details are successfully recorded.", Toast.LENGTH_LONG).show();
				
				Intent i = new Intent(UserProfile.this, DashBoard.class);
                startActivity(i);
				
			}});
		
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
		prov=province.get(position);

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

}
