package games.buendia.jhon.golazzos.queryService;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jhon on 19/01/16.
 */
public class HttpRequest {

    private VolleyService volley;
    private RequestInterface requestInterface;

    public HttpRequest(RequestInterface r){
        requestInterface = r;
    }

    public void starPostRequest(Context context, String url, JSONObject data_send){

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, data_send, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // TODO Auto-generated method stub
                Log.i("respuesta:", response.toString());
                requestInterface.onSuccessCallBack(response);
                //Intent intent = new Intent(context, WizardOnectivity.class);
                //startActivity(intent);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // TODO Auto-generated method stub
                VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                JSONObject jsonObj = null;

                try {
                    jsonObj = new JSONObject(error.getLocalizedMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                    requestInterface.onErrorCallBack(jsonObj);
                    //Log.i("error:",jsonObj.getString("messages"));


            }
        });
        volley = VolleyService.getInstance(context);
        volley.getRequestQueue().add(jsObjRequest);




    }


}
