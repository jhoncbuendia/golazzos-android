package games.buendia.jhon.golazzos.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import games.buendia.jhon.golazzos.R;

/**
 * Created by User on 20/03/2016.
 */
public class WebViewActivity extends Activity {

    private WebView webViewStatics;
    private ProgressBar progressBarLoader;
    private WebSettings webSettings;
    private LinearLayout cabeceroLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);
        cabeceroLayout = (LinearLayout) findViewById(R.id.cabecero);
        cabeceroLayout.setVisibility(View.GONE);
        webViewStatics = (WebView) findViewById(R.id.webViewStatics);
        webViewStatics.setWebViewClient(new myWebClient());
        webSettings = webViewStatics.getSettings();
        webSettings.setJavaScriptEnabled(true);
        progressBarLoader = (ProgressBar) findViewById(R.id.progressBarWebViewLoad);
        webViewStatics.loadUrl(getString(R.string.sharing_url_twitter));
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;

        }
        public void onPageFinished(WebView view, String url) {
            if (!url.contains("complete"))
                progressBarLoader.setVisibility(View.GONE);
            else {
                Intent intent = new Intent();
                intent.putExtra("twitter", true);
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}