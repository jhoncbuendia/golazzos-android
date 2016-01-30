package games.buendia.jhon.golazzos.queryService;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by jhon on 9/01/16.
 */
public class VolleyService {
    private static VolleyService VolleyS = null;

    //Este objeto es la cola que usará la aplicación
    private RequestQueue mRequestQueue;

    private VolleyService(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        Log.i("instancia", "nueva");
    }

    public static VolleyService getInstance(Context context) {
        if (VolleyS == null) {
            VolleyS = new VolleyService(context);
        }else{
            Log.i("instancia", "ya creada");
        }

        return VolleyS;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
}
