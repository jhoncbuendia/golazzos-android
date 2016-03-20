package games.buendia.jhon.golazzos;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by User on 03/02/2016.
 */
public class GolazzosApplication extends Application {

    private static GolazzosApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        context = this;
    }

    public static GolazzosApplication getInstance() {
        return context;
    }

}
