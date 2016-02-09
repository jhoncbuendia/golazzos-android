package games.buendia.jhon.golazzos.queryService;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import games.buendia.jhon.golazzos.utils.PreferencesHelper;
import games.buendia.jhon.golazzos.utils.ServicesCall;

/**
 * Created by jhon on 19/01/16.
 */
public class HttpRequest {

    private VolleyService volley;
    private RequestInterface requestInterface;
    private ServicesCall servicesCall;

    public HttpRequest(RequestInterface r){
        requestInterface = r;
    }

    public HttpRequest(RequestInterface r, ServicesCall servicesCall){
        requestInterface = r;
        this.servicesCall = servicesCall;
    }

    public void starPostRequest(Context context, String url, JSONObject data_send){

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, data_send, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.i("respuesta:", response.toString());
                requestInterface.onSuccessCallBack(response, servicesCall);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                JSONObject jsonObj = null;

                try {
                    jsonObj = new JSONObject(error.getLocalizedMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                requestInterface.onErrorCallBack(jsonObj);
            }
        });
        volley = VolleyService.getInstance(context);
        volley.getRequestQueue().add(jsObjRequest);
    }

    public void sendAuthenticatedPostRequest(Context context, String url ){

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            requestInterface.onSuccessCallBack(new JSONObject(response), servicesCall);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(error.getLocalizedMessage());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        requestInterface.onErrorCallBack(jsonObj);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Token "+PreferencesHelper.getUserToken());
                return params;
            }
        };

        volley = VolleyService.getInstance(context);
        volley.getRequestQueue().add(postRequest);
    }

    public void startPostRequestAuthenticated(Context context, String url, JSONObject data_send, int idTeam){


        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, data_send, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.i("respuesta:", response.toString());
                requestInterface.onSuccessCallBack(response, servicesCall);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                JSONObject jsonObj = null;

                try {
                    jsonObj = new JSONObject(error.getLocalizedMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                requestInterface.onErrorCallBack(jsonObj);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Token "+PreferencesHelper.getUserToken());
                return params;
            }
        };
        volley = VolleyService.getInstance(context);
        volley.getRequestQueue().add(jsObjRequest);
    }


}
