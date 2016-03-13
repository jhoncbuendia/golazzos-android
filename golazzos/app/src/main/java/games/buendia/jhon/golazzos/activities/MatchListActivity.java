package games.buendia.jhon.golazzos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import games.buendia.jhon.golazzos.GolazzosApplication;
import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.adapters.DrawerAdapterList;
import games.buendia.jhon.golazzos.fragments.MatchListFragment;
import games.buendia.jhon.golazzos.interfaces.BaseMethodsActivity;
import games.buendia.jhon.golazzos.model.Match;
import games.buendia.jhon.golazzos.model.Team;
import games.buendia.jhon.golazzos.model.Tournament;
import games.buendia.jhon.golazzos.npaysdkdemo.MainActivity;
import games.buendia.jhon.golazzos.queryService.BuilderJsonList;
import games.buendia.jhon.golazzos.queryService.HttpRequest;
import games.buendia.jhon.golazzos.queryService.RequestInterface;
import games.buendia.jhon.golazzos.utils.DialogHelper;
import games.buendia.jhon.golazzos.utils.PreferencesHelper;
import games.buendia.jhon.golazzos.utils.ServicesCall;

/**
 * Created by User on 06/02/2016.
 */
public class MatchListActivity extends FragmentActivity implements RequestInterface, BaseMethodsActivity{

    private String url;
    private BuilderJsonList builderJsonList;
    private DrawerLayout mDrawerLayout;
    private FragmentTransaction fragmentTransaction;
    private boolean matchsLoaded = false, teamsLoaded = false, leaguesLoaded = false, filterBets = false;
    private ArrayList<Match> matchArrayList;
    private ArrayList<Team> teamsArrayList;
    private ArrayList<Tournament> tournamentArrayList;
    private int idTournament;
    private String teamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_menu_layout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        idTournament = 0;
        teamName = "";
        url = validateUrlToRequestMatch(getIntent());
        DialogHelper.showLoaderDialog(MatchListActivity.this);
        buildMenu();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HttpRequest h = new HttpRequest(MatchListActivity.this, ServicesCall.MATCHES);
                h.sendAuthenticatedPostRequest(getApplicationContext(), url);
            }
        });
    }

    private void buildMenu() {
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
                startActivity(new Intent(MatchListActivity.this, MainActivity.class));
            }
        });
        ((TextView) findViewById(R.id.textViewCondicion)).setText(getString(resourceLevel));
        ((TextView) findViewById(R.id.textViewUsuario)).setText(String.format(getString(R.string.format_hola), PreferencesHelper.getUserName()));

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

                switch (position){

                    case 1: startActivity(new Intent(GolazzosApplication.getInstance(), StadiumActivity.class));
                            finish();
                            break;

                    case 6:
                        PreferencesHelper.unLogUser();
                        startActivity(new Intent(MatchListActivity.this, SignInActivity.class));
                        finish();
                        break;
                }
            }
        });
    }

    @Override
    public void onSuccessCallBack(JSONObject response, ServicesCall servicesCall) {

        builderJsonList = new BuilderJsonList(response);

        switch (servicesCall) {
            case MATCHES:   try {
                                matchArrayList = builderJsonList.getMatches();
                                matchsLoaded = true;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.tournaments_endpoint));
                                        HttpRequest h = new HttpRequest(MatchListActivity.this, ServicesCall.LEAGUES);
                                        h.sendAuthenticatedPostRequest(getApplicationContext(), url);
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;

            case TEAMS:     teamsLoaded = true;
                            try {
                                if (idTournament != 0)
                                    teamsArrayList = builderJsonList.getTeams();
                                else
                                    teamsArrayList = new ArrayList<Team>();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }

                            break;

            case LEAGUES:   leaguesLoaded = true;
                            try {
                                tournamentArrayList = builderJsonList.getTournaments();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (idTournament == 0) {
                                            url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.teams_endpoint));
                                        }
                                        else {
                                            url = String.format(getString(R.string.format_url_teams_tournament), String.valueOf(idTournament));
                                        }
                                        HttpRequest h = new HttpRequest(MatchListActivity.this, ServicesCall.TEAMS);
                                        h.sendAuthenticatedPostRequest(getApplicationContext(), url);
                                    }
                                });
                            } catch (JSONException e){
                                e.printStackTrace();
                            }
                            break;
        }

        if (matchsLoaded && leaguesLoaded && teamsLoaded){

            MatchListFragment matchListFragment = new MatchListFragment();
            Bundle arguments = new Bundle();
            arguments.putSerializable("matches", matchArrayList);
            arguments.putSerializable("leagues", tournamentArrayList);
            arguments.putSerializable("teams", teamsArrayList);
            arguments.putSerializable("tournament_id", idTournament);
            arguments.putSerializable("filter_beat", filterBets);
            arguments.putSerializable("teamName", teamName);
            matchListFragment.setArguments(arguments);
            DialogHelper.hideLoaderDialog();
            makeTransaction(matchListFragment, getSupportFragmentManager(), R.id.content_frame);

        }
    }

    @Override
    public void onErrorCallBack(JSONObject response) {
        Log.i("Response-error", response.toString());
    }

    @Override
    public void buildMenu(ListView listView) {

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

    private String validateUrlToRequestMatch(Intent intent){

        boolean isFilterTeam = false;
        boolean isFilterTournament = false;
        filterBets = intent.getBooleanExtra("filter_bet", false);

        try {
            isFilterTeam = !intent.getStringExtra("team_name").isEmpty();
        }catch (NullPointerException e){

        }

        try {
            isFilterTournament = intent.getIntExtra("tournament_id", 0) != 0;
        } catch (NullPointerException e){

        }

        if (isFilterTeam && isFilterTournament){
            idTournament = intent.getIntExtra("tournament_id", 0);
            teamName = intent.getStringExtra("team_name");
            return String.format(getString(R.string.format_url_matches_team_name_and_tournament),intent.getStringExtra("team_name").replace(" ","%20"), String.valueOf(idTournament));
        }
        else if (isFilterTournament){
            idTournament = intent.getIntExtra("tournament_id", 0);
            return String.format(getString(R.string.format_url_matches_tournament),String.valueOf(intent.getIntExtra("tournament_id", 0)));
        }
        else if (isFilterTeam){
            return String.format(getString(R.string.format_url_matches_team_name),intent.getStringExtra("team_name").replace(" ","%20"));
        }


        return String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.matches_endpoint));
    }
}