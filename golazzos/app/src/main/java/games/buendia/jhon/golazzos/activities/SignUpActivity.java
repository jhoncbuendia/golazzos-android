package games.buendia.jhon.golazzos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import games.buendia.jhon.golazzos.GolazzosApplication;
import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.queryService.HttpRequest;
import games.buendia.jhon.golazzos.queryService.RequestInterface;
import games.buendia.jhon.golazzos.queryService.VolleyService;
import games.buendia.jhon.golazzos.utils.DialogHelper;
import games.buendia.jhon.golazzos.utils.EmailValidator;
import games.buendia.jhon.golazzos.utils.JSONBuilder;
import games.buendia.jhon.golazzos.utils.ServicesCall;

public class SignUpActivity extends AppCompatActivity implements RequestInterface{

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
        setContentView(R.layout.activity_sign_up);
        callbackManager = CallbackManager.Factory.create();
        ingresar = (Button) findViewById(R.id.crear_cuenta);
        LoginManager.getInstance().logOut();
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_friends"));

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

                EmailValidator validator = new EmailValidator();
                userEditText = (EditText) findViewById(R.id.userup);
                passwordEditText = (EditText) findViewById(R.id.pwdup);
                if (validator.validate(userEditText.getText().toString()) && passwordEditText.getText().toString().length() >= 8) {
                    DialogHelper.showLoaderDialog(SignUpActivity.this);
                    loginrMailUser(userEditText.getText().toString(), passwordEditText.getText().toString());
                }
                else {
                    Toast.makeText(SignUpActivity.this,getString(R.string.validador),Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void loginrMailUser(String email, String pwd){
        url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.users_endpoint));
        jsonBuilder = new JSONBuilder();
        HttpRequest h = new HttpRequest(this);
        h.starPostRequest(getApplicationContext(), url, jsonBuilder.getRegisterUserJSON(email, pwd));
    }

    public void registerFbUser(String token){

        url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.tokens_endpoint));
        jsonBuilder = new JSONBuilder();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBuilder.getFacebookRegisterJSON(token), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(GolazzosApplication.getInstance(), SignInActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.i("error:", error.toString());

            }
        });

        VolleyService v = VolleyService.getInstance(GolazzosApplication.getInstance());
        v.getRequestQueue().add(jsObjRequest);

    }

    @Override
    public void onSuccessCallBack(JSONObject response, ServicesCall servicesCall) {
        DialogHelper.hideLoaderDialog();
        startActivity(new Intent(GolazzosApplication.getInstance(), SignInActivity.class));
        finish();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, InitialActivity.class));
        finish();
    }
}
