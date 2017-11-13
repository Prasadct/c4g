package lk.prasad.crop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import lk.prasad.crop.common.ReadWriteJsonFileUtils;
import lk.prasad.crop.common.SqliteController;

public class DashBoard extends Activity implements OnItemSelectedListener {

    boolean ableToExit = false;

    private GridView gridView;
    private GridViewAdapter gridAdapter;

    static List<Integer> CROP_ID_LIST = new ArrayList<Integer>();

    static List<String> CROP_NAME_EN_LIST = new ArrayList<String>();

    static List<String> CROP_NAME_SI_LIST = new ArrayList<String>();

    static List<String> CROP_NAME_LIST = new ArrayList<String>();

    static List<String> CROP_IMG_URL_LIST = new ArrayList<String>();

    static List<String> CROP_STATUS_LIST = new ArrayList<String>();

    public static AssetManager assetManager;
    public static int languageId;

    private static boolean HAVE_CONNECTION = true;
    TextView tv_type;

    public static ArrayList<String> arrayFiles = new ArrayList<String>();

    public static File directory;

    String[] types = {"All", "Fruit", "Vegetable"};
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
        CROP_STATUS_LIST = SplashScreen.CROP_STATUS_LIST;

        assetManager = getAssets();


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

    public void createCropTypes(String json) {
        JSONArray group_jArray = null;
        try {
            group_jArray = new JSONArray(json);
            CROP_ID_LIST.clear();
            CROP_NAME_EN_LIST.clear();
            CROP_NAME_SI_LIST.clear();
            CROP_IMG_URL_LIST.clear();
            CROP_STATUS_LIST.clear();
            for (int i = 0; i < group_jArray.length(); i++) {

                JSONObject json_data = group_jArray.getJSONObject(i);

                CROP_ID_LIST.add(json_data.getInt("id"));
                CROP_NAME_EN_LIST.add(json_data.getString("crop_name_en"));
                CROP_NAME_SI_LIST.add(json_data.getString("crop_name_si"));
                CROP_IMG_URL_LIST.add(json_data.getString("image"));
                CROP_STATUS_LIST.add(json_data.getString("status"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadCropTypes() {

        CROP_NAME_LIST.clear();
        if (CROP_NAME_SI_LIST.size() == 0) {
            connectionLost();
        }
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
                if (SplashScreen.CROP_STATUS_LIST.get(position).equalsIgnoreCase("INACTIVE")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "සමාවන්න! මෙම වගා කාණ්ඩය සදහා තොරතුරු ඇතුලත් කර නොමැත.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    ableToExit = false;
                    int itemValue = SplashScreen.CROP_ID_LIST.get(position);
                    cropId = (position + 1);
                    Intent i = new Intent(DashBoard.this, MainActivity.class);
                    i.putExtra("cropid", itemValue);
                    if (languageId == 2) {
                        i.putExtra("crop_name", SplashScreen.CROP_NAME_SI_LIST.get(position));
                    } else {
                        i.putExtra("crop_name", SplashScreen.CROP_NAME_EN_LIST.get(position));
                    }
                    startActivity(i);
                }
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
                        new InputStreamReader(is, "UTF-8"), 8);
                StringBuilder group_sb = new StringBuilder();
                String group_line = null;
                while ((group_line = group_reader.readLine()) != null) {
                    group_sb.append(group_line + "\n");
                }
                is.close();
                json = group_sb.toString();
                Log.i("jsonnn", json);

                createCroptypes(json);

                ReadWriteJsonFileUtils readWriteJsonFileUtils = new ReadWriteJsonFileUtils(DashBoard.this);
                readWriteJsonFileUtils.deleteFile("crop_types");
                readWriteJsonFileUtils.createJsonFileData("crop_types", json);

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
                HAVE_CONNECTION = false;
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

    public void createCroptypes(String json) {

        JSONArray group_jArray = null;
        try {
            group_jArray = new JSONArray(json);

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
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // connection loss dialog
    public void connectionLost() {
        ReadWriteJsonFileUtils readWriteJsonFileUtils = new ReadWriteJsonFileUtils(DashBoard.this);
        String json = readWriteJsonFileUtils.readJsonFileData("crop_types");
        if (json != null) {
            createCropTypes(json);
        } else {
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
    }

    private int getLanguage() {
        SqliteController sqliteController = new SqliteController(DashBoard.this);
        return sqliteController.getLanguage();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        if (isNetworkConnected()) {
            type = position;
            FakeNetLoader fl = new FakeNetLoader();
            fl.execute(getResources().getString(R.string.service_url) + "crops?cropType=" + type + "&generatedfrom=dashboard&id=" + lk.prasad.crop.common.ProfileData.getPhoneID((TelephonyManager) getSystemService(this.TELEPHONY_SERVICE)));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.prof_menu) {
            Intent intent = new Intent(DashBoard.this,
                    UserProfile.class);
            startActivity(intent);
        } else if (id == R.id.key_search_menu) {
            Intent intent = new Intent(this.getApplicationContext(),
                    KeyWordSearch.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
