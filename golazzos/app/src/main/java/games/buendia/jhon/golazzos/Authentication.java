package games.buendia.jhon.golazzos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import games.buendia.jhon.golazzos.npaysdkdemo.MainActivity;
import games.buendia.jhon.golazzos.queryService.VolleyService;

/**
 * Created by jhon on 9/01/16.
 */
public class Authentication {

    //private String url;
    private JsonArrayRequest request;
    private Context current_context;
    private AccessToken accessToken;
    private JSONArray resp = null;
    private String email;

    public Authentication(Context c){
        current_context = c;

    }



    public void authenticate(final Activity activity, VolleyService volley, AccessToken access_token) {

        accessToken = access_token;
        String url = "https://www.golazzos.com/api/v2/tokens/";


        JSONObject js = new JSONObject();
        try {
            JSONObject token = new JSONObject();

            token.put("from", "facebook");
            token.put("value", accessToken.getToken().toString());


            JSONObject jsonobject = new JSONObject();

            jsonobject.put("token", token);
            //jsonobject.put("request", jsonobject_one);


            js.put("token", token);
            Log.i("JSON", js.toString());

        }catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,url, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Jwt", response.toString());


                        GraphRequest request = GraphRequest.newMeRequest(
                                accessToken,
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {

                                        try {
                                            email =  object.getString("email");
                                            //Log.i("email", object.getString("email"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Intent intent = new Intent(activity, MainActivity.class);



                                        intent.putExtra("email", email);
                                        ///intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        //((Activity)context).finish();
                                        activity.startActivity(intent);
                                        //Intent intent = new Intent(getAplt, MainActivity.class);
                                        //startActivity(intent);

                                        // Application code
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email");
                        request.setParameters(parameters);
                        request.executeAsync();


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("", "respuestarror: " + error.getMessage());

            }
        });

        volley.getRequestQueue().add(jsonObjReq);

    }

}
