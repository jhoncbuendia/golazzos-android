package games.buendia.jhon.golazzos.utils;

import android.app.ProgressDialog;
import android.content.Context;
import games.buendia.jhon.golazzos.R;

/**
 * Created by User on 03/02/2016.
 */
public class DialogHelper {

    private static ProgressDialog progressDialog;

    public static void showLoaderDialog(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.loader_descarga));
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    public static void hideLoaderDialog(){
        progressDialog.hide();
    }

}