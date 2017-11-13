package lk.prasad.crop;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lk.prasad.crop.common.SqliteController;

public class UserProfile extends Activity implements OnItemSelectedListener {

	List<String> province = new ArrayList<String>();
	String[] province_en = { "යාපනය", "කිලිනොච්චි", "මන්නාරම", "මුලතිව්", "වවුනියා", "පුත්තලම", "කුරැනෑගල", "අනුරාධපුර",
            "පොළොන්නරුව" , "ත්\u200Dරිකුණාමලේ", "මඩකළපුව","අම්පාර","ගමිපහ","කොළඔ","කළුතර","කෑගල්ල","රත්නපුර",
            "මාතලේ","මහනුවර","නුවරඑළිය","බදුල්ල","මොනරාගල","ගාල්ල","මාතර","හම්බන්තොට" };
	String[] province_si = { "යාපනය", "කිලිනොච්චි", "මන්නාරම", "මුලතිව්", "වවුනියා", "පුත්තලම", "කුරැනෑගල", "අනුරාධපුර",
            "පොළොන්නරුව" , "ත්\u200Dරිකුණාමලේ", "මඩකළපුව","අම්පාර","ගමිපහ","කොළඔ","කළුතර","කෑගල්ල","රත්නපුර",
            "මාතලේ","මහනුවර","නුවරඑළිය","බදුල්ල","මොනරාගල","ගාල්ල","මාතර","හම්බන්තොට" };

	TextView tv_profile, tv_name, tv_phone, tv_province, tv_email, tv_address;
	
	EditText et_name,et_tel,et_email, et_address;
	
	String id="",name="",tel="",prov="",email="", address="";
    int provId = 1500;

	ImageButton btn_add;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile);

        id = lk.prasad.crop.common.ProfileData.getPhoneID((TelephonyManager)getSystemService(this.TELEPHONY_SERVICE));
        getProfileDataFromDb();
		tv_profile = (TextView) findViewById(R.id.tv_profile);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_province = (TextView) findViewById(R.id.tv_province);
		tv_email = (TextView) findViewById(R.id.tv_email);
        tv_address = (TextView) findViewById(R.id.tv_address);
		
		
		et_name = (EditText) findViewById(R.id.et_name);
		et_tel = (EditText) findViewById(R.id.et_tel);
		et_email = (EditText) findViewById(R.id.et_email);
        et_address = (EditText) findViewById(R.id.et_address);

        if (name != null){
            et_name.setText(name);
        }
        if (tel != null){
            et_tel.setText(tel);
        }
        if (email != null){
            et_email.setText(email);
        }
        if (address != null){
            et_address.setText(address);
        }

		
		
		btn_add=(ImageButton)findViewById(R.id.btn_add);
		
		if (getLanguage() == 2) {
			province = Arrays.asList(province_si);
			btn_add.setBackgroundResource(R.drawable.add_si);
			
			tv_profile.setText("ඔබගේ තොරතුරු");
			tv_name.setText("නම ");
			tv_phone.setText("දු.ක.");
			tv_province.setText("දිස්ත්\u200Dරික්කය");
            tv_address.setText("ලිපිනය ");

			/*Typeface font = Typeface.createFromAsset(getAssets(), "FM-BINDU.TTF");
			tv_profile.setTypeface(font);
			tv_name.setTypeface(font);
			tv_phone.setTypeface(font);
			tv_province.setTypeface(font);*/
			

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
		

        if (provId > 0 && provId < 1000){
            spinner.setSelection(provId);
        }
		
		btn_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				name=et_name.getText().toString();
				tel=et_tel.getText().toString();
				email=et_email.getText().toString();
                address = et_address.getText().toString();
				
				ContentValues cv = new ContentValues();
                cv.put("id", id);
				cv.put("name", name);
				cv.put("tel", tel);
				cv.put("province", prov);
				cv.put("email", email);
                cv.put("provinceid", provId);
                cv.put("address", address);
                if (getPhoneIdFromDb() != null){
                    SqliteController sqliteController =  new SqliteController(UserProfile.this);
                    sqliteController.update(cv, id);
                    System.out.println("updated...");
                } else {
                    SqliteController sqliteController =  new SqliteController(UserProfile.this);
                    sqliteController.insert(cv, "profile");
                    System.out.println("inserted...");
                }
				
                UpdateProfile updateProfile = new UpdateProfile();
                URI nameUri = null;
                try {
                    nameUri = new URI(Uri.encode(name.replaceAll(" ", "%20")));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                URI telUri = null;
                try {
                    telUri = new URI(Uri.encode(tel.replaceAll(" ", "%20")));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                URI provUri = null;
                try {
                    provUri = new URI(Uri.encode(prov.replaceAll(" ", "%20")));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                URI addressUri = null;
                try {
                    addressUri = new URI(Uri.encode(address.replaceAll(" ", "%20")));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                updateProfile.execute(getResources().getString(R.string.service_url)+"users/updateuser?id="+id+"&name="+nameUri+
                        "&phone="+telUri+"&district="+provUri+"&address="+addressUri);
				
				Intent i = new Intent(UserProfile.this, DashBoard.class);
                startActivity(i);
				
			}});
		
	}

	private int getLanguage() {
		SqliteController sqliteController = new SqliteController(UserProfile.this);
        return sqliteController.getLanguage();
	}

    public String getPhoneIdFromDb(){
        SqliteController sqliteController = new SqliteController(UserProfile.this);
        return sqliteController.getPhoneIdFromDb();
    }

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		prov=province.get(position);
        provId=position;

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

    public void getProfileDataFromDb() {
        SqliteController sqliteController = new SqliteController(UserProfile.this);
        List<String> result = sqliteController.getProfileDataFromDb(id);
        if (result != null && result.size() > 0) {
            try {
                id = result.get(0);
                name = result.get(1);
                tel = result.get(2);
                prov = result.get(3);
                email = result.get(4);
                provId = Integer.valueOf(result.get(5));
                address = result.get(6);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        }


    private class UpdateProfile extends AsyncTask<String, Void, List<String>> {

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
                httpPost.setHeader(HTTP.CONTENT_TYPE,
                        "application/x-www-form-urlencoded;charset=UTF-8");

                ArrayList<NameValuePair> group_nameValuePairs = new ArrayList<NameValuePair>();
                group_nameValuePairs.add(new BasicNameValuePair("id",
                        String.valueOf(id)));
                group_nameValuePairs.add(new BasicNameValuePair("name",
                        name));
                group_nameValuePairs.add(new BasicNameValuePair("phone",
                        tel));
                group_nameValuePairs.add(new BasicNameValuePair("district",
                        prov));
                group_nameValuePairs.add(new BasicNameValuePair("provinceid",
                        String.valueOf(provId)));
                group_nameValuePairs.add(new BasicNameValuePair("email",
                        email+""));
                group_nameValuePairs.add(new BasicNameValuePair("address",
                        address+""));
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

            } catch (UnsupportedEncodingException e) {
                Log.e("error ", "Error  " + e.toString());
            } catch (ClientProtocolException e) {
                Log.e("error ", "Error  " + e.toString());
            } catch (IOException e) {
                Log.e("error ", "Error  " + e.toString());

            } catch (Exception e) {
                Log.e("error ", "Error  " + e.toString());
            }

            return title_list;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), "User details are successfully recorded.", Toast.LENGTH_LONG).show();
            finish();
        }

    }


}
