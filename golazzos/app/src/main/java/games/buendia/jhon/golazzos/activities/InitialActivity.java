package games.buendia.jhon.golazzos.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import android.content.pm.Signature;
import android.widget.TextView;
import android.widget.Toast;

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

import games.buendia.jhon.golazzos.GolazzosApplication;
import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.queryService.BuilderJsonList;
import games.buendia.jhon.golazzos.queryService.HttpRequest;
import games.buendia.jhon.golazzos.queryService.RequestInterface;
import games.buendia.jhon.golazzos.utils.DialogHelper;
import games.buendia.jhon.golazzos.utils.JSONBuilder;
import games.buendia.jhon.golazzos.utils.PreferencesHelper;
import games.buendia.jhon.golazzos.utils.ServicesCall;

public class InitialActivity extends AppCompatActivity implements RequestInterface {

    private Button create;
    private Button login;
    private CallbackManager callbackManager;
    private Button ingresar;
    private String url;
    private JSONBuilder jsonBuilder;
    private LoginButton loginButton;
    private TextView textViewFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!PreferencesHelper.isUserLogged()) {

            FacebookSdk.sdkInitialize(getApplicationContext());
            setContentView(R.layout.activity_initial);
            url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.tokens_endpoint));

            callbackManager = CallbackManager.Factory.create();
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
                    Toast.makeText(GolazzosApplication.getInstance(), getString(R.string.error_facebook), Toast.LENGTH_LONG).show();
                }
            });

            textViewFacebook = (TextView) findViewById(R.id.textViewFacebook);
            textViewFacebook.setClickable(true);
            textViewFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginButton.performClick();
                }
            });

            try {
                PackageInfo info = getPackageManager().
                        getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);

                for (Signature signature : info.signatures) {

                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());


                    Log.d("====Hash Key===", Base64.encodeToString(md.digest(),
                            Base64.DEFAULT));

                }

            } catch (PackageManager.NameNotFoundException e) {

            } catch (NoSuchAlgorithmException ex) {

            }

            create = (Button) findViewById(R.id.create);
            login = (Button) findViewById(R.id.login);

            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(intent);
                    finish();

                }
            });
        }
        else {
            Intent intent = new Intent(this, MatchListActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void registerFbUser(String token){
        jsonBuilder = new JSONBuilder();
        DialogHelper.showLoaderDialog(this);
        HttpRequest h = new HttpRequest(this, ServicesCall.LOGIN);
        h.starPostRequest(getApplicationContext(), url, jsonBuilder.getFacebookRegisterJSON(token));
    }

    @Override
    public void onSuccessCallBack(JSONObject response, ServicesCall serviceCall) {
        HttpRequest h;
        BuilderJsonList builderJsonList;

        switch (serviceCall) {

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

                PreferencesHelper.storeUserObjectIntoPreferences(response.getJSONObject(getString(R.string.response)));
                PreferencesHelper.storeSoulTeamIntoPreferences(response.getJSONObject(getString(R.string.response)));

                if (builderJsonList.isWizardCompleted()){
                    startActivity(new Intent(GolazzosApplication.getInstance(), MatchListActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(GolazzosApplication.getInstance(), WizardOnectivity.class));
                    finish();
                }

            } catch (JSONException e) {
                startActivity(new Intent(GolazzosApplication.getInstance(), WizardOnectivity.class));
                finish();
            }
                break;
        }
    }

    @Override
    public void onErrorCallBack(JSONObject response) {
        DialogHelper.hideLoaderDialog();
        try {
            JSONObject jo = response.getJSONObject("messages");
            showAlertDialog(jo.getString("base").replace("[", "").replace("]", "").replace("\"", ""));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showAlertDialog(String mensaje){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getString(R.string.error_titulo));
        alertDialogBuilder.setMessage(mensaje);

        alertDialogBuilder.setPositiveButton(getString(R.string.aceptar_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}