package games.buendia.jhon.golazzos;

import android.app.Application;
import android.util.Log;

import com.flurry.android.FlurryAgent;

/**
 * Created by User on 03/02/2016.
 */
public class GolazzosApplication extends Application {

    private static GolazzosApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .withLogLevel(Log.INFO)
                .withContinueSessionMillis(5000L)
                .withCaptureUncaughtExceptions(false)
                .withPulseEnabled(true)
                .build(context, "G3N7XTG6P6W5PVF8BKC5");
    }

    public static GolazzosApplication getInstance() {
        return context;
    }

}
