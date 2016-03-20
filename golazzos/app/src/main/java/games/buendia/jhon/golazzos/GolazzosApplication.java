package games.buendia.jhon.golazzos;

import android.app.Application;

/**
 * Created by User on 03/02/2016.
 */
public class GolazzosApplication extends Application {

    private static GolazzosApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static GolazzosApplication getInstance() {
        return context;
    }

}
