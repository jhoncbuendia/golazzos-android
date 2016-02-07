package games.buendia.jhon.golazzos.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.queryService.BuilderJsonList;
import games.buendia.jhon.golazzos.queryService.HttpRequest;
import games.buendia.jhon.golazzos.queryService.RequestInterface;
import games.buendia.jhon.golazzos.utils.DialogHelper;

/**
 * Created by User on 06/02/2016.
 */
public class TestActivity extends FragmentActivity implements RequestInterface{

    private String url;
    private BuilderJsonList builderJsonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.matches_endpoint));
        DialogHelper.showLoaderDialog(TestActivity.this);

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
       builderJsonList = new BuilderJsonList(response);
        try {
            Log.i("arry => ", ""+builderJsonList.getMatches().size());
            DialogHelper.hideLoaderDialog();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorCallBack(JSONObject response) {
        Log.i("Response-error", response.toString());
    }
}
