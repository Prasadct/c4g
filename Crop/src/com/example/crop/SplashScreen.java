package com.example.crop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashScreen extends Activity {

	/*--SQLite object to invoke query --*/
	public static SQLiteDatabase DB;

	/*--Data base name --*/
	public static String DB_NAME = "aaa";

	/*--Connection status variable --*/
	private static boolean HAVE_CONNECTION = true;

	private static boolean fistLoad = false;

	static List<Integer> CROP_ID_LIST = new ArrayList<Integer>();

	static List<String> CROP_NAME_EN_LIST = new ArrayList<String>();

	static List<String> CROP_NAME_SI_LIST = new ArrayList<String>();
	
	static List<String> CROP_IMG_URL_LIST = new ArrayList<String>();

	// Splash screen timer
	private static int SPLASH_TIME_OUT = 3000;

	private static boolean isAsyncTaskDone = false;
	private static boolean isThreadDone = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		createDB();

		fistLoad = checkForFirstTime();

		FakeNetLoader fl = new FakeNetLoader();
		fl.execute("http://128.199.125.48/GetCropDetails.php");

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
                if (HAVE_CONNECTION){
                    isThreadDone = true;
                    invokeHome();

                    finish();
                }
			}
		}, SPLASH_TIME_OUT);
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
				isAsyncTaskDone = true;
				invokeHome();
			} else {
				connectionLost();
			}

		}

	}

	// connection loss dialog
	public void connectionLost() {

		AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
		builder.setTitle("Connection Error");
		builder.setMessage("No Internet connection. Please check your internet connection and try again.");
		builder.setIcon(R.drawable.internet);
		builder.setCancelable(false);

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				//startActivityForResult(new Intent(
					//	android.provider.Settings.ACTION_SETTINGS), 0);

				System.exit(0);
			}
		});
		builder.show();

	}

	private void invokeHome() {

		if (isAsyncTaskDone && isThreadDone) {
			if (fistLoad) {
				Intent i = new Intent(SplashScreen.this, DashBoard.class);
				startActivity(i);
			} else {
				Intent i = new Intent(SplashScreen.this, Language.class);
				startActivity(i);
			}
		}
	}

	public void createDB() {

		// create database
		DB = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);

		DB.execSQL("CREATE TABLE IF NOT EXISTS language (language int) ;");
		DB.execSQL("CREATE TABLE IF NOT EXISTS profile (name varchar,tel varchar,province varchar,email varchar) ;");

	}

	/*
	 * @@ Check for first time
	 * 
	 * @@
	 */

	public boolean checkForFirstTime() {

		Cursor c = DB.rawQuery("SELECT * FROM language", null);

		if (c.getCount() > 0) {
			return true;
		}

		c.close();

		return false;
	}

}