package com.example.crop;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ReportDedeases extends Activity implements LocationListener{
	private ImageView mImage;
	EditText et_des;
	ImageButton btn_photo, btn_submit;
	private static final int CAMERA_PIC_REQUEST = 1111;

	static String filePath = "";
	static String des = "";
	TextView tv_main;

	static String imageName;
	private int serverResponseCode = 0;
	
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
    
    protected Button retrieveLocationButton;
    private LocationManager locationManager;
    private String provider;
    Location location;
    
    static double latitude;
    static double longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_deseases);

		getlocation();
		
		tv_main=(TextView)findViewById(R.id.tv_main);
		
		et_des = (EditText) findViewById(R.id.et_des);
		mImage = (ImageView) findViewById(R.id.camera_image);
		btn_photo = (ImageButton) findViewById(R.id.btn_photo);
		btn_submit = (ImageButton) findViewById(R.id.btn_submit);

		if(DashBoard.languageId==2){
			btn_submit.setBackgroundResource(R.drawable.send_si);
			tv_main.setText("frda. jd�;d ls�u");
			Typeface font = Typeface.createFromAsset(DashBoard.assetManager,
					"FM-BINDU.TTF");
			tv_main.setTypeface(font);
		}
		else{
			btn_submit.setBackgroundResource(R.drawable.send_en);
		}
		
		
		btn_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, CAMERA_PIC_REQUEST);
			}
		});

		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!filePath.equals("") && filePath != null) {
					FakeNetLoader fl = new FakeNetLoader();
					fl.execute(filePath);
				}

				des = et_des.getText().toString();
				if (!des.equals("") && des != null) {
					ReportDeseases fl = new ReportDeseases();
					fl.execute("http://128.199.125.48/InsertFieldDetails.php");
				}

				
			}
		});

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_PIC_REQUEST) {
			// 2
			Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
			mImage.setImageBitmap(thumbnail);
			// 3
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
			// 4

			String filepath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/Crop";
			File file = new File(filepath);
			if (!file.exists()) {
				file.mkdir();
			}

			Random rn = new Random();
			int num = rn.nextInt(10000000) + 1;
			String date = new Date().toString();
			imageName = date + num + ".jpg";

			File file1 = new File(file, imageName);
			filePath = file1.getPath();

			try {
				file1.createNewFile();
				FileOutputStream fo = new FileOutputStream(file1);
				// 5
				fo.write(bytes.toByteArray());
				fo.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private class FakeNetLoader extends AsyncTask<String, Void, List<String>> {

		InputStream is = null;
		String json = "";
		List<String> title_list = new ArrayList<String>();

		@Override
		protected List<String> doInBackground(String... urls) {

			uploadFile(urls[0]);
			return title_list;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			super.onPostExecute(result);

		}

	}

	public int uploadFile(String sourceFileUri) {

		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		if (!sourceFile.isFile()) {

			// dialog.dismiss();
			//
			// Log.e("uploadFile", "Source File not exist :"+imagepath);
			//
			runOnUiThread(new Runnable() {
				public void run() {
					// messageText.setText("Source File not exist :"+
					// imagepath);
				}
			});

			return 0;

		} else {
			try {

				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL("http://128.199.125.48/file_upload.php");

				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", fileName);

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
						+ fileName + "\"" + lineEnd);

				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {

					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				// Log.i("uploadFile", "HTTP Response is : "
				// + serverResponseMessage + ": " + serverResponseCode);
				//
				if (serverResponseCode == 200) {

					runOnUiThread(new Runnable() {
						public void run() {
						}
					});
				}

				// close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();

			} catch (MalformedURLException ex) {

				// dialog.dismiss();
				ex.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
						// messageText.setText("MalformedURLException Exception : check script url.");
						// Toast.makeText(MainActivity.this,
						// "MalformedURLException", Toast.LENGTH_SHORT).show();
					}
				});

				Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
			} catch (Exception e) {

				// dialog.dismiss();
				e.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
						// messageText.setText("Got Exception : see logcat ");
						// Toast.makeText(MainActivity.this,
						// "Got Exception : see logcat ",
						// Toast.LENGTH_SHORT).show();
					}
				});
				Log.e("Upload file to server Exception",
						"Exception : " + e.getMessage(), e);
			}
			// dialog.dismiss();
			return serverResponseCode;

		} // End else block
	}

	private class ReportDeseases extends AsyncTask<String, Void, List<String>> {

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
				group_nameValuePairs.add(new BasicNameValuePair("image",
						imageName));
				group_nameValuePairs.add(new BasicNameValuePair("description",
						des));
				group_nameValuePairs.add(new BasicNameValuePair("latitude",
						latitude+""));
				group_nameValuePairs.add(new BasicNameValuePair("longitude",
						longitude+""));
				imageName = "";
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
			Toast.makeText(getApplicationContext(),
					"Disease is successfully reported", Toast.LENGTH_LONG)
					.show();

		}

	}

	
	
	 private Location getlocation(){
	    	try
	        {  
	    		
	    		
	            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

	            // getting GPS status
	           boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	            // getting network status
	           boolean  isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

	            if (!isGPSEnabled && !isNetworkEnabled)
	            {
	                // no network provider is enabled
	            }
	            else
	            {
	               // this.canGetLocation = true;
	                if (isNetworkEnabled)
	                {
	                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,  this);
//	                    Log.d("Network", "Network");
	                    if (locationManager != null)
	                    {
	                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	                        if (location != null)
	                        {
	                            latitude = location.getLatitude();
	                            longitude = location.getLongitude();
	                        }
	                    }
	                }
	                // if GPS Enabled get lat/long using GPS Services
	                if (isGPSEnabled)
	                {
	                    if (location == null)
	                    {
	                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,  this);
//	                        Log.d("GPS Enabled", "GPS Enabled");
	                        if (locationManager != null)
	                        {
	                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	                            if (location != null)
	                            {
	                                latitude = location.getLatitude();
	                                longitude = location.getLongitude();
	                            }
	                        }
	                    }
	                }
	            }

	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }

	        return location;

	   
	    }

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	
	
	
}
