package com.example.crop;

import com.example.crop.R;
import com.example.crop.R.id;
import com.example.crop.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by Prasadct on 9/19/2015.
 */
public class WebViewActivity extends Activity {
	private WebView webView;
	private String html = "<html><body>CropAdvisor <b>Code4Good</b> Hackathon.</body></html>";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view);
		webView = (WebView) findViewById(R.id.webView);
		webView.loadData(html, "text/html", null);
	}
}