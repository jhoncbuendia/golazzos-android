package games.buendia.jhon.golazzos.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.queryService.HttpRequest;
import games.buendia.jhon.golazzos.queryService.RequestInterface;
import games.buendia.jhon.golazzos.utils.PreferencesHelper;

/**
 * Created by User on 06/02/2016.
 */
public class TestActivity extends Activity implements RequestInterface{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        Log.i("token", PreferencesHelper.getUserToken());
        final String url = getString(R.string.url_base)+"matches";

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HttpRequest h = new HttpRequest(TestActivity.this);
                h.sendAuthenticatedPostRequest(getApplicationContext(), url);
            }
        });
    }

    @Override
    public void onSuccessCallBack(JSONObject response) {
        Log.i("Response", response.toString());
    }

    @Override
    public void onErrorCallBack(JSONObject response) {
        Log.i("Response-error", response.toString());
    }
}
