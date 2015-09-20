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
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class Disease extends Activity{
	
	public static String[] details_si;
	public static String[] details_en;
	public static String[] name_si;
	public static String[] name_en;
	public static String[] image ;
	public static int[] ids ;
	
	TextView tv_main;
	ListView list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.disease);
		
		tv_main=(TextView)findViewById(R.id.tv_main);
		
		FakeNetLoader fl = new FakeNetLoader();
		fl.execute("http://128.199.125.48/GetDetailsForMainType.php?mainType=Disease&cropId="+DashBoard.cropId);
		
	}
	
	
	private void loadList(){
		DiseasesList adapter = new DiseasesList(Disease.this,
				setMailList(DashBoard.languageId), image);
		list = (ListView) findViewById(R.id.listView);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(Disease.this,
						WebViewActivity.class);
				intent.putExtra("url", "http://128.199.125.48/Disease.php?cropId="+DashBoard.cropId+"&id="+ids[position]);
				startActivity(intent);

			}
		});
	}
	
	
	private String[] setMailList(int language) {

		if (language == 1) {
			return name_si;
		} else if (language == 2) {
			tv_main.setText("frda.hka");
			Typeface font = Typeface.createFromAsset(DashBoard.assetManager,
					"FM-BINDU.TTF");
			tv_main.setTypeface(font);
			return name_en;
		}
		return name_en;
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
				
				int size=group_jArray.length();
				
				details_si=new String[size] ;
				details_en=new String[size] ;
				name_si=new String[size] ;
				name_en=new String[size] ;
				image =new String[size] ;
				ids =new int[size] ;
				
				for (int i = 0; i < group_jArray.length(); i++) {

					JSONObject json_data = group_jArray.getJSONObject(i);

					details_si[i]=json_data.getString("details_si");
					details_en[i]=json_data.getString("details_en");
					name_si[i]=json_data.getString("name_si");
					name_en[i]=json_data.getString("name_en");
					image[i]=json_data.getString("image");
					ids[i]=json_data.getInt("id");
				}

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();

			} catch (Exception e) {
				Log.e("error ", "Error  " + e.toString());
			}

			return title_list;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			super.onPostExecute(result);

			loadList();
			

		}

	}

	

}
