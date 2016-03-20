package games.buendia.jhon.golazzos.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import games.buendia.jhon.golazzos.GolazzosApplication;
import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.queryService.BuilderJsonList;
import games.buendia.jhon.golazzos.queryService.HttpRequest;
import games.buendia.jhon.golazzos.queryService.RequestInterface;
import games.buendia.jhon.golazzos.utils.DialogHelper;
import games.buendia.jhon.golazzos.utils.EmailValidator;
import games.buendia.jhon.golazzos.utils.JSONBuilder;
import games.buendia.jhon.golazzos.utils.PreferencesHelper;
import games.buendia.jhon.golazzos.utils.ServicesCall;
import games.buendia.jhon.golazzos.utils.TypeScreen;

public class SignInActivity extends AppCompatActivity implements RequestInterface, GoogleApiClient.OnConnectionFailedListener {

    private LoginButton loginButton;
    private EditText userEditText;
    private EditText passwordEditText;
    private CallbackManager callbackManager;
    private CheckBox checkBoxTerminosYCondiciones;
    private Button ingresar, wizardFour;
    private String url;
    private JSONBuilder jsonBuilder;
    private Intent intent;
    private TypeScreen typeScreen;
    private Button create;
    private Button login;
    private TextView textViewFacebook;
    private LinearLayout linearLayoutFacebook, linearLayoutMail, linearLayoutTwitter;
    private ImageView imageViewFacebook, imageViewMail, imageViewTwitter;
    private final int REQUEST_CODE_FACEBOOK = 64206;
    private final int REQUEST_CODE_FACEBOOK_INVITE = 64213;
    private GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_INVITE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();
        callbackManager = CallbackManager.Factory.create();
        intent = getIntent();

        typeScreen = (TypeScreen) intent.getSerializableExtra("typeScreen");
        url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.tokens_endpoint));

        if (!PreferencesHelper.isUserLogged()) {

            if (typeScreen == null) {
                setContentView(R.layout.activity_initial);
                typeScreen = TypeScreen.MAIN_SCREEN;
                initFacebookButton();
                initUI(typeScreen);
            } else {
                switch (typeScreen) {
                    case MAIN_SCREEN:
                        setContentView(R.layout.activity_initial);
                        break;
                    case LOGIN_SCREEN:
                        setContentView(R.layout.activity_sign_in);
                        break;
                    case REGISTER_SCREEN:
                        setContentView(R.layout.activity_sign_up);
                        break;

                }
                initFacebookButton();
                initUI(typeScreen);
            }
        }
        else {
            if (typeScreen == null) {
                Intent intent = new Intent(this, MatchListActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                setContentView(R.layout.activity_wizard_three);
                initUI(typeScreen);
            }
        }
    }

    private void initFacebookButton(){
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
    }

    private void initUI(TypeScreen typeScreen){

        switch (typeScreen){

            case LOGIN_SCREEN: ingresar = (Button)findViewById(R.id.ingresar_cuenta);
                               ingresar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {

                                        EmailValidator validator = new EmailValidator();
                                        userEditText = (EditText) findViewById(R.id.userin);
                                        passwordEditText = (EditText) findViewById(R.id.pwdin);

                                        if (validator.validate(userEditText.getText().toString()) && passwordEditText.getText().toString().length() >= 8) {
                                            DialogHelper.showLoaderDialog(SignInActivity.this);
                                            loginrMailUser(userEditText.getText().toString(), passwordEditText.getText().toString());
                                        }
                                        else {
                                            Toast.makeText(SignInActivity.this,getString(R.string.validador),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                break;

            case MAIN_SCREEN:   textViewFacebook = (TextView) findViewById(R.id.textViewFacebook);
                                textViewFacebook.setClickable(true);
                                textViewFacebook.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        loginButton.performClick();
                                    }
                                });

                                create = (Button) findViewById(R.id.create);
                                login = (Button) findViewById(R.id.login);

                                create.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                        intent.putExtra("typeScreen", TypeScreen.REGISTER_SCREEN);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                                login.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                        intent.putExtra("typeScreen", TypeScreen.LOGIN_SCREEN);
                                        startActivity(intent);
                                        finish();

                                    }
                                });
                                break;

            case REGISTER_SCREEN: ingresar = (Button) findViewById(R.id.crear_cuenta);
                                  checkBoxTerminosYCondiciones = (CheckBox) findViewById(R.id.checkBoxTerminosYCondiciones);
                                  ingresar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View arg0) {

                                            if (checkBoxTerminosYCondiciones.isChecked()) {
                                                EmailValidator validator = new EmailValidator();
                                                userEditText = (EditText) findViewById(R.id.userup);
                                                passwordEditText = (EditText) findViewById(R.id.pwdup);
                                                if (validator.validate(userEditText.getText().toString()) && passwordEditText.getText().toString().length() >= 8 && userEditText.getText().toString().length() < 20) {
                                                    DialogHelper.showLoaderDialog(SignInActivity.this);
                                                    registerMailUser(userEditText.getText().toString(), passwordEditText.getText().toString());
                                                } else {
                                                    Toast.makeText(SignInActivity.this, getString(R.string.validador), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                            else{
                                                showAlertDialog(getString(R.string.debes_aceptar));
                                            }
                                        }
                                    });
                                    break;

            case WIZARD_THREE:       mGoogleApiClient = new GoogleApiClient.Builder(this)
                                     .addApi(AppInvite.API)
                                     .enableAutoManage(this, this)
                                     .build();

                                    boolean autoLaunchDeepLink = true;
                                    AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, autoLaunchDeepLink)
                                            .setResultCallback(
                                                    new ResultCallback<AppInviteInvitationResult>() {
                                                        @Override
                                                        public void onResult(AppInviteInvitationResult result) {
                                                            Log.d("TAG", "getInvitation:onResult:" + result.getStatus());
                                                        }
                                                    });

                                    linearLayoutFacebook = (LinearLayout) findViewById(R.id.linearLayoutFacebook);
                                    linearLayoutMail = (LinearLayout) findViewById(R.id.linearLayoutMail);
                                    linearLayoutTwitter = (LinearLayout) findViewById(R.id.linearLayoutTwitter);

                                    linearLayoutFacebook.setClickable(true);
                                    linearLayoutFacebook.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            openFacebookInvitesDialog();
                                        }
                                    });

                                    linearLayoutMail.setClickable(true);
                                    linearLayoutMail.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            onInviteClicked();
                                        }
                                    });
                                    linearLayoutTwitter.setClickable(true);

                                    imageViewFacebook = (ImageView) findViewById(R.id.imageViewFacebook);
                                    imageViewTwitter = (ImageView) findViewById(R.id.imageViewTwitter);
                                    imageViewMail = (ImageView) findViewById(R.id.imageViewMail);

                                    wizardFour = (Button)findViewById(R.id.buttonSiguiente);
                                    wizardFour.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View arg0) {
                                            Intent intent = new Intent(SignInActivity.this, WizardFourActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                    break;
        }
    }

    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder("Invitacion")
                .setMessage("Descarga la app de golazzos")
                .setDeepLink(Uri.parse("http://example.com/offer/five_dollar_offer"))
                .setCustomImage(Uri.parse("https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png"))
                .setCallToActionText("Install!")
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    public void registerMailUser(String email, String pwd){
        url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.users_endpoint));
        jsonBuilder = new JSONBuilder();
        HttpRequest h = new HttpRequest(this);
        h.starPostRequest(getApplicationContext(), url, jsonBuilder.getRegisterUserJSON(email, pwd));
    }

    public void loginrMailUser(String email, String pwd){
        jsonBuilder = new JSONBuilder();
        HttpRequest h = new HttpRequest(this, ServicesCall.LOGIN);
        h.starPostRequest(getApplicationContext(), url, jsonBuilder.getSingleRegisterJSON(email, pwd));
    }

    public void registerFbUser(String token){
        jsonBuilder = new JSONBuilder();
        DialogHelper.showLoaderDialog(SignInActivity.this);
        HttpRequest h = new HttpRequest(this, ServicesCall.LOGIN);
        h.starPostRequest(getApplicationContext(), url, jsonBuilder.getFacebookRegisterJSON(token));
    }

    @Override
    public void onSuccessCallBack(JSONObject response, ServicesCall servicesCall) {

        HttpRequest h;
        BuilderJsonList builderJsonList;

        switch (typeScreen) {

            case MAIN_SCREEN:
            case LOGIN_SCREEN:  switch (servicesCall) {

                                    case LOGIN:
                                        try {
                                            PreferencesHelper.storeUserInPreferences(response.getJSONObject(getString(R.string.response)));
                                            h = new HttpRequest(this, ServicesCall.ME);
                                            url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.me_endpoint));
                                            h.sendAuthenticatedPostRequest(getApplicationContext(), url);
                                        } catch (JSONException e) {
                                            // TODO - Implementar manager de mensajes de error.
                                        }
                                        break;

                                    case ME:
                                        try {
                                            builderJsonList = new BuilderJsonList(response.getJSONObject(getString(R.string.response)));

                                            PreferencesHelper.storeUserObjectIntoPreferences(response.getJSONObject(getString(R.string.response)));
                                            PreferencesHelper.storeSoulTeamIntoPreferences(response.getJSONObject(getString(R.string.response)));
                                            PreferencesHelper.storeLevelUserIntoPreferences(response.getJSONObject(getString(R.string.response)));

                                            if (builderJsonList.isWizardCompleted()) {
                                                startActivity(new Intent(GolazzosApplication.getInstance(), MatchListActivity.class));
                                                finish();
                                            } else {
                                                startActivity(new Intent(GolazzosApplication.getInstance(), WizardOnectivity.class));
                                                finish();
                                            }

                                        } catch (JSONException e) {
                                            startActivity(new Intent(GolazzosApplication.getInstance(), WizardOnectivity.class));
                                            finish();
                                        }
                                        break;
                                }
                                break;

            case REGISTER_SCREEN: DialogHelper.hideLoaderDialog();
                                  Intent intentScreen = new Intent(GolazzosApplication.getInstance(), SignInActivity.class);
                                  intentScreen.putExtra("typeScreen", TypeScreen.LOGIN_SCREEN);
                                  startActivity(intentScreen);
                                  finish();
                                  break;
        }
    }

    @Override
    public void onErrorCallBack(JSONObject response) {
        DialogHelper.hideLoaderDialog();
        try {
            JSONObject jo = response.getJSONObject("messages");
            if (typeScreen == TypeScreen.LOGIN_SCREEN)
                showAlertDialog(jo.getString("base").replace("[", "").replace("]", "").replace("\"", ""));
            else
                showAlertDialog("Este email "+ jo.getString("email").replace("[", "").replace("]", "").replace("\"", ""));

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_FACEBOOK || requestCode == REQUEST_CODE_FACEBOOK_INVITE) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                linearLayoutMail.setClickable(false);
                linearLayoutMail.setBackgroundColor(Color.parseColor("#d2ff00"));
                imageViewMail.setImageResource(R.drawable.mail_logo_blue);
            } else {
                Toast.makeText(SignInActivity.this, getString(R.string.error_invitacion_mail), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        switch (typeScreen){
            case MAIN_SCREEN:
                super.onBackPressed();
                finish();
                break;
            case LOGIN_SCREEN:
            case REGISTER_SCREEN: startActivity(new Intent(this, SignInActivity.class));
                                  finish();
                                  break;
        }
    }

    public void openFacebookInvitesDialog(){

        if (AppInviteDialog.canShow()) {

            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(getString(R.string.url_app))
                    .setPreviewImageUrl(getString(R.string.image_canvas_url))
                    .build();

            AppInviteDialog appInviteDialog = new AppInviteDialog(this);

            appInviteDialog.registerCallback(callbackManager,
                    new FacebookCallback<AppInviteDialog.Result>() {
                        @Override
                        public void onSuccess(AppInviteDialog.Result result) {
                            linearLayoutFacebook.setClickable(false);
                            linearLayoutFacebook.setBackgroundColor(Color.parseColor("#d2ff00"));
                            imageViewFacebook.setImageResource(R.drawable.facebook_logo_blue);
                        }

                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onError(FacebookException e) {
                            Toast.makeText(SignInActivity.this, getString(R.string.error_invitacion_facebook), Toast.LENGTH_SHORT).show();
                        }
                    });

            appInviteDialog.show(content);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}