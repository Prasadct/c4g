package lk.prasad.crop;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.FilePart;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.google.gson.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;

import lk.prasad.crop.common.ProfileData;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ReportDedeases extends Activity implements LocationListener {
	private ImageView mImage;
	EditText et_des;
	ImageButton btn_photo, btn_submit;
	ImageButton btn_audio;
	ImageButton btn_audio_play;
	String AudioSavePathInDevice = null;
	MediaRecorder mediaRecorder ;
	Random random ;
	String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
	public static final int RequestPermissionCode = 1;
	MediaPlayer mediaPlayer ;
	private static boolean recording = false;
	private static boolean playing = false;

	private static final int CAMERA_PIC_REQUEST = 1111;
	private static boolean HAVE_CONNECTION = true;

	static String filePath = "";
	static String filePathAudio = "";
	static boolean attachAudio = false;
	File audioFileToUpload = null;
	static String des = "";
	TextView tv_main;

	static String imageName;
	private int serverResponseCode = 0;

	static int cropId;
	static String cropName;

	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds

	protected Button retrieveLocationButton;
	private LocationManager locationManager;
	private String provider;
	Location location;
	ProgressDialog dialog = null;
	String upLoadServerUri = null;

	static double latitude;
	static double longitude;

	private static String reqId;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_deseases);

		checkConnection();
		getlocation();

		tv_main = (TextView) findViewById(R.id.tv_main);

		et_des = (EditText) findViewById(R.id.et_des);
		mImage = (ImageView) findViewById(R.id.camera_image);
		btn_photo = (ImageButton) findViewById(R.id.btn_photo);
		btn_submit = (ImageButton) findViewById(R.id.btn_submit);
		btn_audio = (ImageButton) findViewById(R.id.btn_start_recording);
		btn_audio_play = (ImageButton) findViewById(R.id.btn_play_recording);
		btn_audio_play.setEnabled(false);

		random = new Random();

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			cropId = 0;
			cropName = null;
		} else {
			cropId = extras.getInt("cropid");
			cropName = extras.getString("crop_name");
		}

		if (DashBoard.languageId == 2) {
			btn_submit.setBackgroundResource(R.drawable.send_si);
			tv_main.setText(cropName);
			/*Typeface font = Typeface.createFromAsset(DashBoard.assetManager,
					"FM-BINDU.TTF");
			tv_main.setTypeface(font);*/
		} else {
			btn_submit.setBackgroundResource(R.drawable.send_en);
		}


		btn_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, CAMERA_PIC_REQUEST);
			}
		});

		btn_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String phoneId = getPhoeId();
				reqId = phoneId;
				des = et_des.getText().toString();
				URI uri = null;
				try {
					uri = new URI(Uri.encode(des.replaceAll(" ", "%20")));
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				upLoadServerUri = getResources().getString(R.string.service_url) + "uploads/uploadimage?reqid=" + reqId +"&content=" + uri;
				dialog = ProgressDialog.show(ReportDedeases.this, "", "Uploading...", true);

				String fileAudio =
						Environment.getExternalStorageDirectory()
								.getAbsolutePath() + "/Crop/" +
								"AudioRecording.3gp";
				File file = new File(fileAudio);
				if (!file.exists() || !attachAudio) {
					filePathAudio = "";
				} else {
					filePathAudio = fileAudio;
					attachAudio = false;
				}
				if (HAVE_CONNECTION) {

					UploadImage uploadImage = new UploadImage();
					uploadImage.execute(getResources().getString(R.string.service_url) + "uploads?reqid=" + reqId);

				} else {
					dialog.dismiss();
					connectionLost();
				}


			}


		});

		/*
		* https://www.tutorialspoint.com/android/android_audio_capture.htm
		* */
		btn_audio.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!recording){
					if(checkPermission()) {
						attachAudio = false;
						btn_submit.setEnabled( false);
						String filepath = Environment.getExternalStorageDirectory()
								.getAbsolutePath() + "/Crop";
						File file = new File(filepath);
						if (!file.exists()) {
							file.mkdir();
						}
						AudioSavePathInDevice =
								Environment.getExternalStorageDirectory()
										.getAbsolutePath() + "/Crop/" +
										 "AudioRecording.3gp";

						File fileR = new File(AudioSavePathInDevice);
						if (!fileR.exists()) {
							fileR.delete();
						}

						MediaRecorderReady();

						try {
							mediaRecorder.prepare();
							mediaRecorder.start();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

//						btn_audio.setEnabled(false);
//					buttonStop.setEnabled(true);
						btn_audio.setImageResource(R.drawable.audio_stop_recording);
						btn_audio.setScaleType(ImageView.ScaleType.FIT_CENTER);
						recording = true;
						Toast.makeText(ReportDedeases.this, "Recording started",
								Toast.LENGTH_LONG).show();
					} else {
						requestPermission();
					}
				} else {
					mediaRecorder.stop();
//					buttonStop.setEnabled(false);
//					buttonPlayLastRecordAudio.setEnabled(true);
//					buttonStart.setEnabled(true);
//					buttonStopPlayingRecording.setEnabled(false);
					btn_audio.setImageResource(R.drawable.audio_start_recording);
					btn_audio.setScaleType(ImageButton.ScaleType.FIT_CENTER);
					recording = false;
					btn_audio_play.setEnabled(true);
					Toast.makeText(ReportDedeases.this, "Recording Completed",
							Toast.LENGTH_LONG).show();
					attachAudio = true;
					btn_submit.setEnabled(true);
				}

			}
		});

		btn_audio_play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) throws IllegalArgumentException,
					SecurityException, IllegalStateException {

				if (!recording && !playing){
					btn_audio.setEnabled(false);
					btn_audio_play.setImageResource(R.drawable.audio_pause);
					btn_audio_play.setScaleType(ImageButton.ScaleType.FIT_CENTER);

					mediaPlayer = new MediaPlayer();
					try {
						mediaPlayer.setDataSource(AudioSavePathInDevice);
						mediaPlayer.prepare();
					} catch (IOException e) {
						e.printStackTrace();
					}

					mediaPlayer.start();
					playing = true;
					Toast.makeText(ReportDedeases.this, "Recording Playing",
							Toast.LENGTH_LONG).show();
				} else {
					playing = false;
					btn_audio.setEnabled(true);
					btn_audio_play.setImageResource(R.drawable.audio_play);
					btn_audio_play.setScaleType(ImageButton.ScaleType.FIT_CENTER);

					if(mediaPlayer != null){
						mediaPlayer.stop();
						mediaPlayer.release();
						MediaRecorderReady();
					}
				}

			}
		});
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_PIC_REQUEST && data != null && data.getExtras() != null) {
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
			imageName = num + ".jpg";

			File file1 = new File(file, imageName);
			filePath = file1.getPath();

			try {
				boolean bol = file1.createNewFile();
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

	private String getPhoeId() {
		String phoneID = ProfileData.phoneId;
		if (phoneID != null) {
			return phoneID;
		} else {
			phoneID = new UserProfile().getPhoneIdFromDb();
			if (phoneID != null) {
				return phoneID;
			} else {
				return String.valueOf(new Random().nextInt(10000000));
			}
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"ReportDedeases Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app URL is correct.
				Uri.parse("android-app://lk.prasad.crop/http/host/path")
		);
		AppIndex.AppIndexApi.start(client, viewAction);
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"ReportDedeases Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app URL is correct.
				Uri.parse("android-app://lk.prasad.crop/http/host/path")
		);
		AppIndex.AppIndexApi.end(client, viewAction);
		client.disconnect();
	}

	/*private class FakeNetLoader extends AsyncTask<String, Void, List<String>> {

		InputStream is = null;
		String json = "";
		List<String> title_list = new ArrayList<String>();

        ProgressDialog progressBar;
        private int progressBarStatus = 0;
        private Handler progressBarHandler = new Handler();
        Context context;

        private long fileSize = 0;

        FakeNetLoader(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute(){
            // prepare for a progress bar dialog
            progressBar = new ProgressDialog(context);
            progressBar.setCancelable(true);
            progressBar.setMessage("Image uploading ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressBar.setProgress(0);
            progressBar.setMax(100);
            progressBar.show();

            //reset progress bar status
            progressBarStatus = 0;

            //reset filesize
            fileSize = 0;
        }

		@Override
		protected List<String> doInBackground(String... urls) {
            String sourceFileUri = urls[0];
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

//                return 0;

            } else {
                try {

                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(
                            sourceFile);
                    URL url = new URL(getResources().getString(R.string.service_url)+"uploads/uploadimage");

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
                    conn.setRequestProperty("uploaded_file", sourceFileUri);
                    conn.setRequestProperty("image_name", imageName);

                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                            + sourceFileUri + "\"" + lineEnd);

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
                        progressBar.setProgress((bytesAvailable / maxBufferSize)*100);
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
//                return serverResponseCode;

            } // End else block
			return title_list;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			super.onPostExecute(result);
            // close the progress bar dialog
            progressBar.dismiss();
		}

	}

	public int uploadFile(String sourceFileUri) {

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
				URL url = new URL(getResources().getString(R.string.service_url)+"uploads/uploadimage");

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
				conn.setRequestProperty("uploaded_file", sourceFileUri);

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
						+ sourceFileUri + "\"" + lineEnd);

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
	}*/

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
				group_nameValuePairs.add(new BasicNameValuePair("cropid",
						String.valueOf(cropId)));
				group_nameValuePairs.add(new BasicNameValuePair("crop_name",
						cropName));
				group_nameValuePairs.add(new BasicNameValuePair("image",
						imageName));
				group_nameValuePairs.add(new BasicNameValuePair("description",
						des));
				group_nameValuePairs.add(new BasicNameValuePair("latitude",
						latitude + ""));
				group_nameValuePairs.add(new BasicNameValuePair("longitude",
						longitude + ""));
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
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
				}
			});

			Toast.makeText(getApplicationContext(),
					"Disease is successfully reported", Toast.LENGTH_LONG)
					.show();
			finish();
		}

	}


	private class UploadImage extends AsyncTask<String, Void, List<String>> {

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
				if (filePath.length() == 0){
					filePath = "message.png";
				}
				if (des.length() == 0 ){
					des = "--";
				}
				final File f = new File(filePath);
				if (filePathAudio.length() > 0){
					audioFileToUpload = new File(filePathAudio);
				}


				new Thread(new Runnable() {
					@Override
					public void run() {
						List <Part> files = new ArrayList();
							files.add(new FilePart("userPhoto", f));
						if (audioFileToUpload != null) {
							files.add(new FilePart("userPhoto", audioFileToUpload));
						}

						if (files.size() == 1) {
							Future uploading = Ion.with(ReportDedeases.this)
									.load(upLoadServerUri)
									.setMultipartFile("userPhoto", f)
									.asString()
									.withResponse()
									.setCallback(new FutureCallback<Response<String>>() {
										@Override
										public void onCompleted(Exception e, Response<String> result) {
											des = et_des.getText().toString();
											if (!des.equals("") && des != null) {
												ReportDeseases fl = new ReportDeseases();
												fl.execute(getResources().getString(R.string.service_url) + "uploads?reqid=" + reqId + "&content=" + des);
											}
										}
									});
						} else if (files.size() == 2){
							Ion.with(ReportDedeases.this)
									.load(upLoadServerUri)
									.addMultipartParts(files)
									.asJsonObject()
									.setCallback(new FutureCallback < JsonObject > () {
										@Override
										public void onCompleted(Exception e, JsonObject result) {}
									});
						}


//				if (!filePath.equals("") && filePath != null) {
//					FakeNetLoader fl = new FakeNetLoader(ReportDedeases.this);
//					fl.execute(filePath);
//				}

					}
				}).start();
			} catch (Exception e) {
				Log.e("error ", "Error  " + e.toString());
			}

			return title_list;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			super.onPostExecute(result);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
				}
			});

			Toast.makeText(getApplicationContext(),
					"Disease is successfully reported", Toast.LENGTH_LONG)
					.show();
			finish();
		}

	}


	private Location getlocation() {
		try {


			locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

			// getting GPS status
			boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
			} else {
				// this.canGetLocation = true;
				if (isNetworkEnabled) {
					if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
						// TODO: Consider calling
						//    ActivityCompat#requestPermissions
						// here to request the missing permissions, and then overriding
						//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
						//                                          int[] grantResults)
						// to handle the case where the user grants the permission. See the documentation
						// for ActivityCompat#requestPermissions for more details.
						return null;
					}
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, this);
//	                    Log.d("Network", "Network");
					if (locationManager != null) {
						location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, this);
//	                        Log.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}

		} catch (Exception e) {
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
						new InputStreamReader(is, "UTF-8"), 8);
				StringBuilder group_sb = new StringBuilder();
				String group_line = null;
				while ((group_line = group_reader.readLine()) != null) {
					group_sb.append(group_line + "\n");
				}
				is.close();
				json = group_sb.toString();
				Log.i("jsonnn", json);
				HAVE_CONNECTION = true;
			} catch (UnsupportedEncodingException e) {
				Log.e("error ", "Error  " + e.toString());
				HAVE_CONNECTION = false;
			} catch (ClientProtocolException e) {
				Log.e("error ", "Error  " + e.toString());
				HAVE_CONNECTION = false;
			} catch (IOException e) {
				Log.e("error ", "Error  " + e.toString());
				HAVE_CONNECTION = false;

			} catch (Exception e) {
				Log.e("error ", "Error  " + e.toString());
			}

			return title_list;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			super.onPostExecute(result);

		}

	}

	private void checkConnection() {
		FakeNetLoader fl = new FakeNetLoader();
		fl.execute(getResources().getString(R.string.service_url) + "crops");
	}

	// connection loss dialog
	public void connectionLost() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ReportDedeases.this);
		builder.setTitle("Connection Error");
		builder.setMessage("No Internet connection. Please check your internet connection and try again.");
		builder.setIcon(R.drawable.internet);
		builder.setCancelable(false);

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				startActivityForResult(new Intent(
						Settings.ACTION_SETTINGS), 0);

				System.exit(0);
			}
		});
		builder.show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		// noinspection SimplifiableIfStatement
		if (id == R.id.message){
			Intent intent = new Intent(ReportDedeases.this, WebViewActivity.class);
			intent.putExtra("url", "http://www.cropadvisor.site/forum_ind?generatedfrom=dashboard&id="+ lk.prasad.crop.common.ProfileData.getPhoneID((TelephonyManager)getSystemService(this.TELEPHONY_SERVICE)));
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu. reply, menu);
		return true;
	}

	public void MediaRecorderReady(){
		mediaRecorder=new MediaRecorder();
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
		mediaRecorder.setOutputFile(AudioSavePathInDevice);
	}

	public String CreateRandomAudioFileName(int string){
		StringBuilder stringBuilder = new StringBuilder( string );
		int i = 0 ;
		while(i < string ) {
			stringBuilder.append(RandomAudioFileName.
					charAt(random.nextInt(RandomAudioFileName.length())));

			i++ ;
		}
		return stringBuilder.toString();
	}

	private void requestPermission() {
		ActivityCompat.requestPermissions(ReportDedeases.this, new
				String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case RequestPermissionCode:
				if (grantResults.length> 0) {
					boolean StoragePermission = grantResults[0] ==
							PackageManager.PERMISSION_GRANTED;
					boolean RecordPermission = grantResults[1] ==
							PackageManager.PERMISSION_GRANTED;

					if (StoragePermission && RecordPermission) {
						Toast.makeText(ReportDedeases.this, "Permission Granted",
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(ReportDedeases.this,"Permission Denied",Toast.LENGTH_LONG).show();
					}
				}
				break;
		}
	}

	public boolean checkPermission() {
		int result = ContextCompat.checkSelfPermission(getApplicationContext(),
				WRITE_EXTERNAL_STORAGE);
		int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
				RECORD_AUDIO);
		return result == PackageManager.PERMISSION_GRANTED &&
				result1 == PackageManager.PERMISSION_GRANTED;
	}
}
