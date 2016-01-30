package games.buendia.jhon.golazzos.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.queryService.HttpRequest;
import games.buendia.jhon.golazzos.queryService.RequestInterface;
import games.buendia.jhon.golazzos.queryService.VolleyService;

public class SignUpActivity extends AppCompatActivity implements RequestInterface{

    private TextView ingresar;
    private Context context;
    private Button crear_cuenta;

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    public void registerMailUser(String email, String pwd){
        JSONObject js = new JSONObject();
        JSONObject user_data = new JSONObject();
        try {
            user_data.put("email", email);
            user_data.put("password", pwd);
            user_data.put("first_name", email);
            user_data.put("last_name", email);


            js = new JSONObject();
            js.put("user", user_data);
            Log.i("created jsson", "" + js);
        }catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "https://www.golazzos.com/api/v2/users";

        HttpRequest h = new HttpRequest(this);
        h.starPostRequest(getApplicationContext(), url, js);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void registerFbUser(String token){
        JSONObject js = new JSONObject();
        JSONObject user_data = new JSONObject();
        try {
            user_data.put("from", "facebook");
            user_data.put("value", token);


            js = new JSONObject();
            js.put("token", user_data);
            Log.i("created jsson", "" + js);
        }catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "https://www.golazzos.com/api/v2/tokens";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // TODO Auto-generated method stub
                Log.i("respuesta:", response.toString());
                Intent intent = new Intent(context, WizardOnectivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.i("error:", error.toString());

            }
        });

        VolleyService v = VolleyService.getInstance(context);
        v.getRequestQueue().add(jsObjRequest);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_sign_up);
        context = this;

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                AccessToken accessToken = loginResult.getAccessToken();
                //Log.i("token:", accessToken.getToken().toString());
                registerFbUser(accessToken.getToken().toString());
                /*GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                Log.i("Json: ", object.toString());

                                try {
                                    Log.i("Email", object.getString("email").toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(context, WizardOnectivity.class);
                                startActivity(intent);


                                // Application code
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();*/


            }

            @Override
            public void onCancel() {
                //info.setText("Login attempt canceled.");

            }


            @Override
            public void onError(FacebookException e) {
                //info.setText("Login attempt failed.");
                Log.i("exception:", e.toString());

            }
        });

        //registerMailUser();



        crear_cuenta = (Button)findViewById(R.id.crear_cuenta);



        crear_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                TextView user = (TextView) findViewById(R.id.userup);
                TextView pwd = (TextView) findViewById(R.id.pwdup);
                Log.i("user and pass", user.getText().toString() + pwd.getText().toString());
                registerMailUser(user.getText().toString(), pwd.getText().toString() );


            }
        });
        /*SignUpFragment l = new SignUpFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.signup_content, l, "signup").commit();*/



    }


    @Override
    public void onSuccessCallBack(JSONObject response) {
        Log.i("json sucess callback", response.toString() );
        Toast toast = null;
        toast = Toast.makeText(context, response.toString(), Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onErrorCallBack(JSONObject response) {
        try {
            Log.i("json error callback", response.getString("messages") );
            Toast toast = Toast.makeText(context, response.getString("messages"), Toast.LENGTH_LONG);
            toast.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
