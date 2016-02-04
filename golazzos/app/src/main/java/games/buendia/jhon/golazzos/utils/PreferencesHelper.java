package games.buendia.jhon.golazzos.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.json.JSONObject;

import games.buendia.jhon.golazzos.GolazzosApplication;

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

    public static void storeUserInPreferences (JSONObject response){
        
    }
}
