package lk.prasad.crop;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import lk.prasad.crop.common.ReadWriteJsonFileUtils;

/**
 * Created by Prasadct on 9/19/2015.
 */
public class WebViewActivity extends Activity {
    private WebView webView;
    private String html = "<html><body>CropAdvisor <b>Code4Good</b> Hackathon.</body></html>";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);

        Bundle extras = getIntent().getExtras();
        final String url = extras.getString("url");

        webView = (WebView) findViewById(R.id.webView);
        final ReadWriteJsonFileUtils readWriteJsonFileUtils = new ReadWriteJsonFileUtils(WebViewActivity.this);
        html = readWriteJsonFileUtils.readJsonFileData(getCatFromUrl(url));
        /* An instance of this class will be registered as a JavaScript interface */
        class MyJavaScriptInterface {
            @SuppressWarnings("unused")
            @JavascriptInterface
            public void showHTML(String loadedHtml) {
                readWriteJsonFileUtils.createJsonFileData(getCatFromUrl(url), loadedHtml);
            }
        }
        if (!isNetworkConnected() && html != null) {
            //webView.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
            webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
        } else {

/* JavaScript must be enabled if you want it to work, obviously */
            webView.getSettings().setJavaScriptEnabled(true);

/* Register a new JavaScript interface called HTMLOUT */
            webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

/* WebViewClient must be set BEFORE calling loadUrl! */
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
        /* This call inject JavaScript into the page which just finished loading. */
                    webView.loadUrl("javascript:window.HTMLOUT.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                }
            });

/* load a web page */
            webView.loadUrl(url);
        }
        setContentView(webView);
    }

    private String getCatFromUrl(String url) {
        int p = url.lastIndexOf("?");
        String e = url.substring(p + 1);
        return e;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}