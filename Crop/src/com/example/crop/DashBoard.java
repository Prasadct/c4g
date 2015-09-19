package com.example.crop;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

	boolean ableToExit = false;

	private GridView gridView;
	private GridViewAdapter gridAdapter;

	static List<Integer> CROP_ID_LIST = new ArrayList<Integer>();

	static List<String> CROP_NAME_EN_LIST = new ArrayList<String>();

	static List<String> CROP_NAME_SI_LIST = new ArrayList<String>();

	static List<String> CROP_NAME_LIST = new ArrayList<String>();

	static List<String> CROP_IMG_URL_LIST = new ArrayList<String>();

	public static AssetManager assetManager;
	public static int languageId;

	private static boolean HAVE_CONNECTION = true;
	TextView tv_type;

	public static ArrayList<String> arrayFiles = new ArrayList<String>();

	public static File directory;

	String[] types = {"All", "Fruit", "Vegetable" };
	static public int type = 0;
	static public int cropId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);

		
		tv_type = (TextView) findViewById(R.id.tv_type);

		gridView = (GridView) findViewById(R.id.gridView);

		CROP_ID_LIST = SplashScreen.CROP_ID_LIST;
		CROP_NAME_EN_LIST = SplashScreen.CROP_NAME_EN_LIST;
		CROP_NAME_SI_LIST = SplashScreen.CROP_NAME_SI_LIST;
		CROP_IMG_URL_LIST = SplashScreen.CROP_IMG_URL_LIST;

		assetManager =getAssets();
		
		
		languageId = getLanguage();
		if (languageId == 2) {
			tv_type.setText("Tnf.a j.d ldKavh f;darkak");
			Typeface font = Typeface.createFromAsset(getAssets(),
					"FM-BINDU.TTF");
			tv_type.setTypeface(font);

		}
		loadCropTypes();

		ContextWrapper cw = new ContextWrapper(getApplicationContext());

		directory = cw.getDir("funnyVideo", Context.MODE_APPEND);

		File f = new File(directory.toString());
		f.mkdirs();
		File[] file = f.listFiles();

		if (file != null && file.length != 0) {

			for (int i = 0; i < file.length; i++)
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

	public void loadCropTypes() {
		
		CROP_NAME_LIST.clear();
		if (languageId == 2) {
			CROP_NAME_LIST.addAll(CROP_NAME_SI_LIST);
		} else {
			CROP_NAME_LIST.addAll(CROP_NAME_EN_LIST);
		}
		

		gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout,
				CROP_NAME_LIST, CROP_IMG_URL_LIST);
		gridView.setAdapter(gridAdapter);

		// ListView Item Click Listener
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				ableToExit = false;
				int itemValue = SplashScreen.CROP_ID_LIST.get(position);
				cropId=(position+1);
				Intent i = new Intent(DashBoard.this, MainActivity.class);
				i.putExtra("cropid", itemValue);
				startActivity(i);
			}

		});
	}

	protected boolean isAppInstalled(String packageName) {
		Intent mIntent = getPackageManager().getLaunchIntentForPackage(
				packageName);
		if (mIntent != null) {
			return true;
		} else {
			return false;
		}
	}

	// exit for back button
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (ableToExit) {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				DashBoard.this.startActivity(intent);

				finish();
				System.exit(0);

			} else {
				ableToExit = true;
				Toast.makeText(getApplicationContext(), "Press again to exit",
						Toast.LENGTH_LONG).show();
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private class FakeNetLoader extends AsyncTask<String, Void, List<String>> {

		InputStream is = null;
		String json = "";
		List<String> title_list = new ArrayList<String>();

		@Override
		protected List<String> doInBackground(String... urls) {

			title_list = getOutputFromUrl(urls[0]);
			return title_list;
		}

		private List<String> getOutputFromUrl(String url) {

			try {

				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);

				// get values for group table form remote db

				ArrayList<NameValuePair> group_nameValuePairs = new ArrayList<NameValuePair>();

				httpPost.setEntity(new UrlEncodedFormEntity(
						group_nameValuePairs));
				HttpResponse group_httpResponse = httpClient.execute(httpPost);
				HttpEntity group_httpEntity = group_httpResponse.getEntity();

				is = group_httpEntity.getContent();

				BufferedReader group_reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder group_sb = new StringBuilder();
				String group_line = null;
				while ((group_line = group_reader.readLine()) != null) {
					group_sb.append(group_line + "\n");
				}
				is.close();
				json = group_sb.toString();
				Log.i("jsonnn", json);

				JSONArray group_jArray = new JSONArray(json);

				
				CROP_ID_LIST.clear();
				CROP_NAME_EN_LIST.clear();
				CROP_NAME_SI_LIST.clear();
				CROP_IMG_URL_LIST.clear();
				for (int i = 0; i < group_jArray.length(); i++) {

					JSONObject json_data = group_jArray.getJSONObject(i);

					CROP_ID_LIST.add(json_data.getInt("id"));
					CROP_NAME_EN_LIST.add(json_data.getString("crop_name_en"));
					CROP_NAME_SI_LIST.add(json_data.getString("crop_name_si"));
					CROP_IMG_URL_LIST.add(json_data.getString("image"));
				}

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				HAVE_CONNECTION = false;

			} catch (Exception e) {
				Log.e("error ", "Error  " + e.toString());
			}

			return title_list;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			super.onPostExecute(result);

			if (HAVE_CONNECTION) {
				loadCropTypes();
			} else {
				connectionLost();
			}

		}

	}

	// connection loss dialog
	public void connectionLost() {

		AlertDialog.Builder builder = new AlertDialog.Builder(DashBoard.this);
		builder.setTitle("Connection Error");
		builder.setMessage("No Internet connection. Please check your internet connection and try again.");
		builder.setIcon(R.drawable.internet);
		builder.setCancelable(false);

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				startActivityForResult(new Intent(
						android.provider.Settings.ACTION_SETTINGS), 0);

				System.exit(0);
			}
		});
		builder.show();

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
		type = position;
		FakeNetLoader fl = new FakeNetLoader();
		fl.execute("http://128.199.125.48/GetCropDetails.php?cropType="+type);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

}
