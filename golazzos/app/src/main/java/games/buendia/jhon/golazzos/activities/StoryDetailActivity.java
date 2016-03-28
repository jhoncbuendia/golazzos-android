package games.buendia.jhon.golazzos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.adapters.DrawerAdapterList;
import games.buendia.jhon.golazzos.fragments.StoryDetailFragment;
import games.buendia.jhon.golazzos.interfaces.BaseMethodsActivity;
import games.buendia.jhon.golazzos.npaysdkdemo.MainActivity;
import games.buendia.jhon.golazzos.utils.PreferencesHelper;

/**
 * Created by User on 28/02/2016.
 */
public class StoryDetailActivity extends FragmentActivity implements BaseMethodsActivity {

    private DrawerLayout mDrawerLayout;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_menu_layout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        buildMenu(null);

        StoryDetailFragment storyDetailFragment = new StoryDetailFragment();
        Bundle bundleArguments = new Bundle();
        bundleArguments.putSerializable("story", getIntent().getSerializableExtra("story"));
        storyDetailFragment.setArguments(bundleArguments);

        makeTransaction(storyDetailFragment, getSupportFragmentManager(), R.id.content_frame);
    }

    @Override
    public void buildMenu(ListView listView) {

        String[] optionsMenu = {getString(R.string.perfil_menu),
                getString(R.string.estadio_menu),
                getString(R.string.partidos_menu),
                getString(R.string.jugadas_menu),
                getString(R.string.ayuda_menu),
                getString(R.string.golazzos_menu),
                getString(R.string.cerrar_sesion)};

        int resourceLevel = PreferencesHelper.getUserLevel();

        ((ListView) findViewById(R.id.listViewMenu)).setAdapter(new DrawerAdapterList(this, optionsMenu));
        ((TextView) findViewById(R.id.tvPuntos)).setText(String.valueOf(PreferencesHelper.getUserPoints()));
        findViewById(R.id.cardViewQuieroSerTitular).setVisibility(resourceLevel == R.string.suplente ? View.VISIBLE : View.GONE);
        findViewById(R.id.cardViewQuieroSerTitular).setClickable(true);
        findViewById(R.id.cardViewQuieroSerTitular).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StoryDetailActivity.this, MainActivity.class));
            }
        });
        ((TextView) findViewById(R.id.textViewCondicion)).setText(getString(resourceLevel));
        ((TextView) findViewById(R.id.textViewUsuario)).setText(String.format(getString(R.string.format_hola), PreferencesHelper.getUserName()));
        ((TextView) findViewById(R.id.textViewNivelUsuario)).setText(String.format(getString(R.string.format_nivel_menu), String.valueOf(PreferencesHelper.getUserLevelNumber())));
        ImageView imageViewUser = (ImageView) findViewById(R.id.imageViewPictureUser);

        String urlImage = PreferencesHelper.getUrlPhoto();

        if (!urlImage.contains("http")){
            urlImage = "http:"+urlImage;
        }

        Picasso.with(this)
                .load(urlImage)
                .into(imageViewUser);


        ListView listViewMenu = (ListView) findViewById(R.id.listViewMenu);
        listViewMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 6){
                    PreferencesHelper.unLogUser();
                    startActivity(new Intent(StoryDetailActivity.this, SignInActivity.class));
                    finish();
                }
            }
        });
    }

    @Override
    public void makeTransaction(Fragment fragment, FragmentManager fragmentManager, int resourceView) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(resourceView, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void openDrawerMenu(View v) {
        mDrawerLayout.openDrawer((LinearLayout) findViewById(R.id.layoutBaseMenu));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, StadiumActivity.class));
        finish();

    }
}
