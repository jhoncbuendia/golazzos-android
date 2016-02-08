package games.buendia.jhon.golazzos.interfaces;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ListView;

/**
 * Created by User on 25/08/2015.
 */
public interface BaseMethodsActivity {
    public void buildMenu(ListView listView);
    public void makeTransaction(Fragment fragment, FragmentManager fragmentManager, int resourceView);
    public void openDrawerMenu(View v);
}
