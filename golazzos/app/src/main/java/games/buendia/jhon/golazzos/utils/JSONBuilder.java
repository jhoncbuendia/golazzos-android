package games.buendia.jhon.golazzos.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User on 03/02/2016.
 */
public class JSONBuilder {

    private JSONObject jsonObject;
    private JSONObject userData;

    public JSONBuilder(){
        jsonObject = new JSONObject();
        userData = new JSONObject();
    }

    public JSONObject getSingleRegisterJSON(String email, String pwd){
        try {
            userData.put("from", "credentials");
            userData.put("email", email);
            userData.put("password", pwd);
            jsonObject = new JSONObject();
            jsonObject.put("token", userData);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getFacebookRegisterJSON(String token){
        try {
            userData.put("from", "facebook");
            userData.put("value", token);

            jsonObject = new JSONObject();
            jsonObject.put("token", userData);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
