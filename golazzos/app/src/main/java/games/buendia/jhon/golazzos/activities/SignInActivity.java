package games.buendia.jhon.golazzos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import games.buendia.jhon.golazzos.GolazzosApplication;
import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.queryService.BuilderJsonList;
import games.buendia.jhon.golazzos.queryService.HttpRequest;
import games.buendia.jhon.golazzos.queryService.RequestInterface;
import games.buendia.jhon.golazzos.queryService.VolleyService;
import games.buendia.jhon.golazzos.utils.DialogHelper;
import games.buendia.jhon.golazzos.utils.JSONBuilder;
import games.buendia.jhon.golazzos.utils.PreferencesHelper;
import games.buendia.jhon.golazzos.utils.ServicesCall;

public class SignInActivity extends AppCompatActivity implements RequestInterface {
    private LoginButton loginButton;
    private EditText userEditText;
    private EditText passwordEditText;
    private CallbackManager callbackManager;
    private Button ingresar;
    private String url;
    private JSONBuilder jsonBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_sign_in);
        url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.tokens_endpoint));
        callbackManager = CallbackManager.Factory.create();
        ingresar = (Button)findViewById(R.id.ingresar_cuenta);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                AccessToken accessToken = loginResult.getAccessToken();
                registerFbUser(accessToken.getToken().toString());
            }

            @Override
            public void onCancel() {
                // Nothing to do here.
            }


            @Override
            public void onError(FacebookException e) {
                Toast.makeText(GolazzosApplication.getInstance(), getString(R.string.error_facebook) ,Toast.LENGTH_LONG).show();
            }
        });

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                userEditText = (EditText) findViewById(R.id.userin);
                passwordEditText = (EditText) findViewById(R.id.pwdin);
                DialogHelper.showLoaderDialog(SignInActivity.this);
                loginrMailUser(userEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

    }

    public void loginrMailUser(String email, String pwd){
        jsonBuilder = new JSONBuilder();
        HttpRequest h = new HttpRequest(this, ServicesCall.LOGIN);
        h.starPostRequest(getApplicationContext(), url, jsonBuilder.getSingleRegisterJSON(email,pwd));
    }

    public void registerFbUser(String token){

        jsonBuilder = new JSONBuilder();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBuilder.getFacebookRegisterJSON(token), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    PreferencesHelper.storeUserInPreferences(response.getJSONObject(getString(R.string.response)));
                    startActivity(new Intent(GolazzosApplication.getInstance(), MatchListActivity.class));
                } catch (JSONException e){
                    // TODO - Implementar manager de mensajes de error.
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO - Implementar manager de mensajes de error.
            }
        });

        VolleyService v = VolleyService.getInstance(GolazzosApplication.getInstance());
        v.getRequestQueue().add(jsObjRequest);

    }

    @Override
    public void onSuccessCallBack(JSONObject response, ServicesCall servicesCall) {

        HttpRequest h;
        BuilderJsonList builderJsonList;

        switch (servicesCall) {

            case LOGIN: try {
                            PreferencesHelper.storeUserInPreferences(response.getJSONObject(getString(R.string.response)));
                            h = new HttpRequest(this, ServicesCall.ME);
                            url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.me_endpoint));
                            h.sendAuthenticatedPostRequest(getApplicationContext(), url);
                        } catch (JSONException e) {
                            // TODO - Implementar manager de mensajes de error.
                        }
                        break;

           case ME: try {
                           builderJsonList = new BuilderJsonList(response.getJSONObject(getString(R.string.response)));

                           if (builderJsonList.isWizardCompleted()){
                               startActivity(new Intent(GolazzosApplication.getInstance(), MatchListActivity.class));
                           }
                           else {
                               startActivity(new Intent(GolazzosApplication.getInstance(), WizardOnectivity.class));
                           }

                       } catch (JSONException e) {
                            startActivity(new Intent(GolazzosApplication.getInstance(), WizardOnectivity.class));
                       }
                       break;
        }
    }

    @Override
    public void onErrorCallBack(JSONObject response) {
        DialogHelper.hideLoaderDialog();
        try {
            //TODO - Cambiar toast de mensaje de error por alert dialogs.
            Toast toast = Toast.makeText(GolazzosApplication.getInstance(), response.getString("messages"), Toast.LENGTH_LONG);
            toast.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}