package games.buendia.jhon.golazzos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.fragments.MatchListFragment;
import games.buendia.jhon.golazzos.interfaces.BaseMethodsActivity;
import games.buendia.jhon.golazzos.model.Match;
import games.buendia.jhon.golazzos.model.Team;
import games.buendia.jhon.golazzos.model.Tournament;
import games.buendia.jhon.golazzos.queryService.BuilderJsonList;
import games.buendia.jhon.golazzos.queryService.HttpRequest;
import games.buendia.jhon.golazzos.queryService.RequestInterface;
import games.buendia.jhon.golazzos.utils.DialogHelper;
import games.buendia.jhon.golazzos.utils.ServicesCall;

/**
 * Created by User on 06/02/2016.
 */
public class MatchListActivity extends FragmentActivity implements RequestInterface, BaseMethodsActivity{

    private String url;
    private BuilderJsonList builderJsonList;
    private DrawerLayout mDrawerLayout;
    private FragmentTransaction fragmentTransaction;
    private boolean matchsLoaded = false, teamsLoaded = false, leaguesLoaded = false;
    private ArrayList<Match> matchArrayList;
    private ArrayList<Team> teamsArrayList;
    private ArrayList<Tournament> tournamentArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_menu_layout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        url = validateUrlToRequestMatch(getIntent());
        DialogHelper.showLoaderDialog(MatchListActivity.this);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HttpRequest h = new HttpRequest(MatchListActivity.this, ServicesCall.MATCHES);
                h.sendAuthenticatedPostRequest(getApplicationContext(), url);
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
                                        url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.teams_endpoint));
                                        HttpRequest h = new HttpRequest(MatchListActivity.this, ServicesCall.TEAMS);
                                        h.sendAuthenticatedPostRequest(getApplicationContext(), url);
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;

            case TEAMS:     teamsLoaded = true;
                            try {
                                teamsArrayList = builderJsonList.getTeams();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.tournaments_endpoint));
                                        HttpRequest h = new HttpRequest(MatchListActivity.this, ServicesCall.LEAGUES);
                                        h.sendAuthenticatedPostRequest(getApplicationContext(), url);
                                    }
                                });
                            }catch (JSONException e){
                                e.printStackTrace();
                            }

                            break;

            case LEAGUES:   leaguesLoaded = true;
                            try {
                                tournamentArrayList = builderJsonList.getTournaments();
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
        mDrawerLayout.openDrawer((ListView) findViewById(R.id.listViewMenu));
    }

    private String validateUrlToRequestMatch(Intent intent){

        boolean isFilterTeam = false;
        boolean isFilterTournament = false;

        try {
            isFilterTeam = !intent.getStringExtra("team_name").isEmpty();
        }catch (NullPointerException e){

        }

        try {
            isFilterTournament = intent.getIntExtra("tournament_id", 0) != 0;
        } catch (NullPointerException e){

        }

        if (isFilterTournament){
            return String.format(getString(R.string.format_url_matches_tournament),String.valueOf(intent.getIntExtra("tournament_id", 0)));
        }
        else if (isFilterTeam){
            return String.format(getString(R.string.format_url_matches_team_name),intent.getStringExtra("team_name"));
        }

        return String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.matches_endpoint));
    }
}
