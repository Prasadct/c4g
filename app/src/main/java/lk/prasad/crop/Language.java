package lk.prasad.crop;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class Language extends Activity {

	ImageButton btn_en, btn_si, btn_done;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.language);
        final Context context = getApplication().getBaseContext();

		btn_en = (ImageButton) findViewById(R.id.btn_en);
		btn_si = (ImageButton) findViewById(R.id.btn_si);

		btn_en.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				/*ContentValues cv = new ContentValues();
				cv.put("language", "1");
				SplashScreen.DB.insert("language", null, cv);

				Intent i = new Intent(Language.this, UserProfile.class);
				startActivity(i);*/

                Toast toast = Toast.makeText(context, "Currently only support for Sinhala", Toast.LENGTH_SHORT);
                toast.show();

			} 	
		});

		btn_si.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				ContentValues cv = new ContentValues();
				cv.put("language", "2");
				SplashScreen.DB.insert("language", null, cv);

				Intent i = new Intent(Language.this, UserProfile.class);
				startActivity(i);

			}
		});

	}

}
