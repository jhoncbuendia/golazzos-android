package games.buendia.jhon.golazzos.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.json.JSONException;
import org.json.JSONObject;

import games.buendia.jhon.golazzos.GolazzosApplication;
import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.model.Team;

/**
 * Created by User on 03/02/2016.
 */


public class PreferencesHelper {

    private static SharedPreferences sharedPreferences;
    private static Editor editor;

    private static void instanciateSharedPreferences(){
        sharedPreferences = GolazzosApplication.getInstance().
                getSharedPreferences(ApplicationConstants.keyPreferences, Context.MODE_PRIVATE);
    }

    public static void updateUserPoints(int points){
        instanciateSharedPreferences();
        editor = sharedPreferences.edit();
        editor.putInt(ApplicationConstants.pointsUserKey, points);
        editor.commit();

    }

    public static void storeUserInPreferences(JSONObject response) throws JSONException {
        instanciateSharedPreferences();
        editor = sharedPreferences.edit();
        editor.putString(ApplicationConstants.tokenKey, response.getString(ApplicationConstants.jsonKeyJwt));
        editor.commit();
    }

    public static String getUserToken(){
        instanciateSharedPreferences();
        return sharedPreferences.getString(ApplicationConstants.tokenKey,"");
    }

    public static void storeUserObjectIntoPreferences(JSONObject response) throws JSONException {
        instanciateSharedPreferences();
        editor = sharedPreferences.edit();
        editor.putBoolean(ApplicationConstants.userLoggedKey, true);
        editor.putInt(ApplicationConstants.idUserKey, response.getInt("id"));
        editor.putString(ApplicationConstants.nameUserKey, response.getString("name"));
        editor.putString(ApplicationConstants.emailUserKey, response.getString("email"));
        editor.putInt(ApplicationConstants.pointsUserKey, response.getInt("points"));
        editor.putString(ApplicationConstants.urlImageKey, response.getString("profile_pic_url"));
        editor.putBoolean(ApplicationConstants.paidSubscriptionKey, response.getBoolean("paid_subscription"));
        editor.commit();
    }

    public static void storeSoulTeamIntoPreferences(JSONObject response){
        try {
            instanciateSharedPreferences();
            JSONObject jsonObject = response.getJSONObject("soul_team");
            editor = sharedPreferences.edit();
            editor.putInt(ApplicationConstants.soutTeamIdKey, jsonObject.getInt("id"));
            editor.putString(ApplicationConstants.soutTeamNameKey, jsonObject.getString("name"));
            editor.putString(ApplicationConstants.soulTeamImageUrlKey, jsonObject.getString("image_path"));
            editor.commit();
        }
        catch (JSONException e){
            // Nothing to do here.
        }
    }

    public static void storeSoulTeamIntoPreferences(Team team){
        instanciateSharedPreferences();
        editor = sharedPreferences.edit();
        editor.putInt(ApplicationConstants.soutTeamIdKey, team.getIdTeam());
        editor.putString(ApplicationConstants.soutTeamNameKey, team.getTeamName());
        editor.putString(ApplicationConstants.soulTeamImageUrlKey, team.getUrlTeam());
        editor.commit();
    }

    public static int getIdSoulTeam(){
        instanciateSharedPreferences();
        return sharedPreferences.getInt(ApplicationConstants.soutTeamIdKey, 0);
    }

    public static int getIdUser(){
        instanciateSharedPreferences();
        return sharedPreferences.getInt(ApplicationConstants.idUserKey, 0);
    }

    public static String getUrlSoulTeam(){
        instanciateSharedPreferences();
        return sharedPreferences.getString(ApplicationConstants.soulTeamImageUrlKey, "");
    }

    public static String getNameSoulTeam(){
        instanciateSharedPreferences();
        return sharedPreferences.getString(ApplicationConstants.soutTeamNameKey, "");
    }

    public static String getUserName(){
        instanciateSharedPreferences();
        return sharedPreferences.getString(ApplicationConstants.nameUserKey, "");
    }

    public static int getUserPoints(){
        instanciateSharedPreferences();
        return sharedPreferences.getInt(ApplicationConstants.pointsUserKey, 0);
    }

    public static int getUserLevel(){
        instanciateSharedPreferences();
        if (sharedPreferences.getBoolean(ApplicationConstants.paidSubscriptionKey, false))
            return R.string.titular;
        else return R.string.suplente;
    }

    public static String getUrlPhoto(){
        instanciateSharedPreferences();
        return sharedPreferences.getString(ApplicationConstants.urlImageKey, "");
    }

    public static boolean isUserLogged(){
        instanciateSharedPreferences();
        return sharedPreferences.getBoolean(ApplicationConstants.userLoggedKey, false);
    }

    public static void unLogUser(){
        instanciateSharedPreferences();
        editor = sharedPreferences.edit();
        editor.putBoolean(ApplicationConstants.userLoggedKey, false);
        editor.commit();
    }
}
