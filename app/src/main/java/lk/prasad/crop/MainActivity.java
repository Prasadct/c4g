package lk.prasad.crop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import lk.prasad.crop.common.ProfileData;

public class MainActivity extends Activity {
	ListView list;
	int language = 1;
	String[] mainList;
	int cropId = 0;
	String cropName;

	TextView tv_main;
	
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
			tv_main.setText("ප්‍රධාන මෙනුව ");
			/*Typeface font = Typeface.createFromAsset(DashBoard.assetManager,
					"FM-BINDU.TTF");
			tv_main.setTypeface(font);*/
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
		
		tv_main=(TextView)findViewById(R.id.tv_main);
		
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
					
					/*Intent intent = new Intent(MainActivity.this,
							Disease.class);
					startActivity(intent);*/
//                    Toast toast = Toast.makeText(context, "මෙම සේවාව තාවකාලිකව අත්හිටුවා ඇත ", Toast.LENGTH_SHORT);
//                    toast.show();
					Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
					intent.putExtra("url", getResources().getString(R.string.service_url)+"details?cat="+7+"&generatedfrom=detail&id="+ ProfileData.getPhoneID((TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE)));
					startActivity(intent);
				} else if (position == 2) {
					Intent intent = new Intent(MainActivity.this,
							ReportDedeases.class);
					Bundle extras = getIntent().getExtras();
					if(extras == null) {
						cropId = 0;
						cropName = null;
					} else {
						cropId = extras.getInt("cropid");
						cropName = extras.getString("crop_name");
					}
					intent.putExtra("cropid", cropId);
					intent.putExtra("crop_name", cropName);
					startActivity(intent);
                    /*Toast toast = Toast.makeText(context, "මෙම සේවාව තාවකාලිකව අත්හිටුවා ඇත ", Toast.LENGTH_SHORT);
                    toast.show();*/
				} else if (position == 3) {
					//Toast toast = Toast.makeText(context, "මෙම සේවාව තාවකාලිකව අත්හිටුවා ඇත ", Toast.LENGTH_SHORT);
					//toast.show();
					Intent intent = new Intent(Intent.ACTION_DIAL);
					intent.setData(Uri.parse("tel:1920"));
					startActivity(intent);
				}
			}
		});

		FakeNetLoader fl = new FakeNetLoader();
		fl.execute("http://www.cropadvisor.site/1.png");

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		// noinspection SimplifiableIfStatement
		if (id == R.id.prof_menu){
            Intent intent = new Intent(MainActivity.this,
							UserProfile.class);
					startActivity(intent);
        }else if (id == R.id.key_search_menu){
			Intent intent = new Intent(this.getApplicationContext(),
					KeyWordSearch.class);
			startActivity(intent);
		}

		return super.onOptionsItemSelected(item);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
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

				Bitmap bitmap = getBitmapFromURL(url);
				File checkFile = new File(MainActivity.this.getApplicationInfo().dataDir + "/crop_caches/");
				if (!checkFile.exists()) {
					checkFile.mkdir();
				}

				FileOutputStream out = null;
				try {
					out = new FileOutputStream(checkFile.getAbsolutePath()+"/1.png");
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
					// PNG is a lossless format, the compression factor (100) is ignored
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (out != null) {
							out.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			} catch (Exception e) {
				Log.e("error ", "Error  " + e.toString());
			}

			return title_list;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			super.onPostExecute(result);
		}

		public Bitmap getBitmapFromURL(String src) {
			try {
				java.net.URL url = new java.net.URL(src);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				Bitmap myBitmap = BitmapFactory.decodeStream(input);
				return myBitmap;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

	}
}
