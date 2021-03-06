package games.buendia.jhon.golazzos.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import games.buendia.jhon.golazzos.GolazzosApplication;
import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.adapters.CustomSpinnerAdapter;
import games.buendia.jhon.golazzos.model.Team;
import games.buendia.jhon.golazzos.model.Tournament;
import games.buendia.jhon.golazzos.queryService.BuilderJsonList;
import games.buendia.jhon.golazzos.queryService.HttpRequest;
import games.buendia.jhon.golazzos.queryService.RequestInterface;
import games.buendia.jhon.golazzos.utils.DialogHelper;
import games.buendia.jhon.golazzos.utils.JSONBuilder;
import games.buendia.jhon.golazzos.utils.ServicesCall;

/**
 * Created by User on 28/02/2016.
 */
public class UpdateFavoriteTeamsActivity extends Activity implements RequestInterface, AdapterView.OnItemSelectedListener {

    private Spinner spinnerLigas;
    private Spinner spinnerEquipos;
    private Button buttonSiguiente;
    private Button buttonAgregar;
    private ArrayList<Team> teamArrayList;
    private boolean teamsLoaded = false, leaguesLoaded = false;
    private String url;
    private int idTournament;
    private BuilderJsonList builderJsonList;
    private ArrayList<Team> teamsArrayList;
    private ArrayList<Tournament> tournamentArrayList;
    private ArrayList<Team> favoriteTeams;
    private int check = 0;
    private ImageView firstTeamImageView, secondTeamImageView, thirdTeamImageView, fourTeamImageView,
            fifthTeamImageView, sixTeamImageView, sevenTeamImageView, eightTeamImageView;
    private int teamsToUp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_favorite_teams);
        teamArrayList = new ArrayList<Team>();
        favoriteTeams = new ArrayList<Team>();
        idTournament = 0;

        firstTeamImageView = (ImageView) findViewById(R.id.imageViewFirstTeam);
        secondTeamImageView = (ImageView) findViewById(R.id.imageViewSecondTeam);
        thirdTeamImageView = (ImageView) findViewById(R.id.imageViewThirdTeam);
        fourTeamImageView = (ImageView) findViewById(R.id.imageViewFourTeam);
        fifthTeamImageView = (ImageView) findViewById(R.id.imageViewFifthTeam);
        sixTeamImageView = (ImageView) findViewById(R.id.imageViewSixTeam);
        sevenTeamImageView = (ImageView) findViewById(R.id.imageViewSevenTeam);
        eightTeamImageView = (ImageView) findViewById(R.id.imageViewEightTeam);

        DialogHelper.showLoaderDialog(UpdateFavoriteTeamsActivity.this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                url = getString(R.string.favorites_teams_by_user_url);
                HttpRequest h = new HttpRequest(UpdateFavoriteTeamsActivity.this, ServicesCall.USER_FAVORITES);
                h.sendAuthenticatedPostRequest(getApplicationContext(), url);
            }
        });
    }

    private void initUI() {
        spinnerEquipos = (Spinner) findViewById(R.id.spinnerEquipos);
        spinnerLigas = (Spinner) findViewById(R.id.spinnerLigas);
        buttonSiguiente = (Button) findViewById(R.id.buttonSiguiente);
        buttonAgregar = (Button) findViewById(R.id.buttonAgregarEquipo);

        boolean ifIsSelected = idTournament != 0, findIt = false;
        int indexSelected = 0;
        String[] tournamentsStringArray = new String[tournamentArrayList.size()];

        for (int i = 0; i < tournamentArrayList.size(); i++) {
            tournamentsStringArray[i] = tournamentArrayList.get(i).getNameTornament();
            if (ifIsSelected){
                if (!findIt){
                    if (tournamentArrayList.get(i).getIdTournament() == idTournament) {
                        findIt = true;
                        indexSelected = i;
                    }
                }
            }
        }

        if (ifIsSelected){
            tournamentsStringArray[indexSelected] = tournamentArrayList.get(0).getNameTornament();
            tournamentsStringArray[0] = tournamentArrayList.get(indexSelected).getNameTornament();
        }

        String[] teamsStringArray = new String[teamsArrayList.size()];
        for (int i = 0; i < teamsArrayList.size(); i++)
            teamsStringArray[i] = teamsArrayList.get(i).getTeamName();

        if (idTournament != 0) {
            spinnerLigas.setAdapter(null);
            spinnerEquipos.setAdapter(null);
        }

        spinnerLigas.setAdapter(new CustomSpinnerAdapter(this, tournamentsStringArray, true));
        spinnerEquipos.setAdapter(new CustomSpinnerAdapter(this, teamsStringArray, true));

        spinnerEquipos.setFocusable(false);
        spinnerLigas.setFocusable(false);

        spinnerLigas.setOnItemSelectedListener(this);
        check = 0;

        buttonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favoriteTeams.size() != 0) {
                    teamsToUp = 1;
                    DialogHelper.showLoaderDialog(UpdateFavoriteTeamsActivity.this);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sendFavoriteTeamRequest(favoriteTeams.get(0));
                        }
                    });

                }
                else {
                    Toast.makeText(UpdateFavoriteTeamsActivity.this, getString(R.string.debes_seleccionar_equipo_favorito), Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInTeamList(spinnerEquipos.getSelectedItem().toString())) {
                    Team favoriteTeam = teamsArrayList.get(spinnerEquipos.getSelectedItemPosition());
                    favoriteTeams.add(favoriteTeam);
                    addInRowLayout(spinnerEquipos.getSelectedItem().toString(), teamArrayList.size(), favoriteTeam.getUrlTeam());
                } else {
                    Toast.makeText(UpdateFavoriteTeamsActivity.this, getString(R.string.equipo_agregado), Toast.LENGTH_SHORT).show();
                }
            }
        });

        LinearLayout layoutFanaticada = (LinearLayout) findViewById(R.id.layoutFanaticada);
        LinearLayout layoutFavoritos = (LinearLayout) findViewById(R.id.layoutFavoritos);
        LinearLayout layoutPartidos = (LinearLayout) findViewById(R.id.layoutPartidos);
        LinearLayout layoutRanking = (LinearLayout) findViewById(R.id.layoutRanking);
        LinearLayout layoutAmigos = (LinearLayout) findViewById(R.id.layoutAmigos);

        layoutRanking.setClickable(true);
        layoutRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GolazzosApplication.getInstance(), RankingActivity.class));
                finish();
            }
        });

        layoutPartidos.setClickable(true);
        layoutPartidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GolazzosApplication.getInstance(), MatchListActivity.class));
                finish();
            }
        });

        layoutFanaticada.setClickable(true);
        layoutFanaticada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GolazzosApplication.getInstance(), StadiumActivity.class));
                finish();
            }
        });

        TextView textViewEquipoDelAlma = (TextView) findViewById(R.id.textViewEquipoDelAlma);
        textViewEquipoDelAlma.setClickable(true);
        textViewEquipoDelAlma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GolazzosApplication.getInstance(), UpdateSoulTeamActivity.class));
                finish();
            }
        });

    }

    private void sendFavoriteTeamRequest(Team soulTeam){
        url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.favorite_team_endpoint));
        JSONBuilder jsonBuilder = new JSONBuilder();
        HttpRequest h = new HttpRequest(UpdateFavoriteTeamsActivity.this, ServicesCall.FAVORITE_ADD);
        h.startPostRequestAuthenticated(getApplicationContext(), url, jsonBuilder.getFavoriteTeamJSON(String.valueOf(soulTeam.getIdTeam()), false),soulTeam.getIdTeam());
    }

    private boolean isInTeamList(String teamName){
        for (Team team: teamArrayList){
            if (team.getTeamName().equals(teamName))
                return true;
        }
        teamArrayList.add(new Team(teamName));
        return false;
    }

    private void addInRowLayout(String teamName, int sizeList, String url){
        switch (sizeList){

            case 1: ((TextView) findViewById(R.id.textViewFirstTeam)).setText(teamName);
                putImageIntoPicassoImageView(firstTeamImageView, url);
                break;

            case 2: ((TextView) findViewById(R.id.textViewSecondTeam)).setText(teamName);
                putImageIntoPicassoImageView(secondTeamImageView, url);
                break;

            case 3: ((TextView) findViewById(R.id.textViewThirdTeam)).setText(teamName);
                putImageIntoPicassoImageView(thirdTeamImageView, url);
                break;

            case 4: ((TextView) findViewById(R.id.textViewFourTeam)).setText(teamName);
                putImageIntoPicassoImageView(fourTeamImageView, url);
                break;

            case 5: ((TextView) findViewById(R.id.textViewFifthTeam)).setText(teamName);
                putImageIntoPicassoImageView(fifthTeamImageView, url);
                break;

            case 6: ((TextView) findViewById(R.id.textViewSixTeam)).setText(teamName);
                putImageIntoPicassoImageView(sixTeamImageView, url);
                break;

            case 7: ((TextView) findViewById(R.id.textViewSevenTeam)).setText(teamName);
                putImageIntoPicassoImageView(sevenTeamImageView, url);
                break;

            case 8: ((TextView) findViewById(R.id.textViewEightTeam)).setText(teamName);
                putImageIntoPicassoImageView(eightTeamImageView, url);
                break;

            case 9: Toast.makeText(this, getString(R.string.equipos_agregados_completos),Toast.LENGTH_SHORT).show(); break;
        }
    }

    private void putImageIntoPicassoImageView(final ImageView imageView, String url){

        if (!url.contains("http"))
            url = "http:"+url;

        Picasso.with(this)
                .load(url)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        // Nothing to do here.
                    }

                    @Override
                    public void onError() {
                        imageView.setImageResource(R.drawable.elipse_azul);
                    }
                });
    }

    @Override
    public void onSuccessCallBack(JSONObject response, ServicesCall serviceCall) {
        builderJsonList = new BuilderJsonList(response);
        switch (serviceCall){

            case USER_FAVORITES:
                                try {
                                    ArrayList<Team> favorites = builderJsonList.getFavoriteTeams();
                                    if (!favorites.isEmpty()){
                                        final Team team = favorites.get(0);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                url = String.format(getString(R.string.delete_favorite_team_url), String.valueOf(team.getIdTeam()));
                                                HttpRequest h = new HttpRequest(UpdateFavoriteTeamsActivity.this, ServicesCall.DELETE_FAVORITE_TEAM);
                                                h.sendAuthenticatedDeleteRequest(GolazzosApplication.getInstance(), url);
                                            }
                                        });
                                    }
                                    else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.tournaments_endpoint));
                                                HttpRequest h = new HttpRequest(UpdateFavoriteTeamsActivity.this, ServicesCall.LEAGUES);
                                                h.sendAuthenticatedPostRequest(getApplicationContext(), url);
                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;

            case DELETE_FAVORITE_TEAM:  runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                url = getString(R.string.favorites_teams_by_user_url);
                                                HttpRequest h = new HttpRequest(UpdateFavoriteTeamsActivity.this, ServicesCall.USER_FAVORITES);
                                                h.sendAuthenticatedPostRequest(getApplicationContext(), url);
                                            }
                                        });
                                        break;

            case TEAMS:
                teamsLoaded = true;
                try {
                    if (idTournament != 0)
                        teamsArrayList = builderJsonList.getTeamsWithImages();
                    else
                        teamsArrayList = new ArrayList<Team>();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case LEAGUES:
                leaguesLoaded = true;
                try {
                    tournamentArrayList = builderJsonList.getTournaments();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (idTournament == 0) {
                                url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.teams_endpoint));
                            } else {
                                url = String.format(getString(R.string.format_url_teams_tournament), String.valueOf(idTournament));
                            }
                            HttpRequest h = new HttpRequest(UpdateFavoriteTeamsActivity.this, ServicesCall.TEAMS);
                            h.sendAuthenticatedPostRequest(getApplicationContext(), url);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case FAVORITE_ADD:  teamsLoaded = false;
                if (teamsToUp != favoriteTeams.size()) {
                    final Team team = favoriteTeams.get(teamsToUp);
                    teamsToUp++;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sendFavoriteTeamRequest(team);
                        }
                    });
                }
                break;
        }

        if (idTournament == 0) {
            if (leaguesLoaded && teamsLoaded) {
                initUI();
                DialogHelper.hideLoaderDialog();
            }
        }
        else if (teamsLoaded) {
            initUI();
            DialogHelper.hideLoaderDialog();
        }
        else if (teamsToUp >= favoriteTeams.size()){
            DialogHelper.hideLoaderDialog();
            startActivity(new Intent(this, StadiumActivity.class));
            finish();
        }
    }

    @Override
    public void onErrorCallBack(JSONObject response) {
        DialogHelper.hideLoaderDialog();
        Log.i("resp", response.toString());
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        check = check + 1;
        if(check > 1) {
            idTournament = tournamentArrayList.get(i).getIdTournament();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    url = String.format(getString(R.string.format_url_teams_tournament), String.valueOf(idTournament));
                    HttpRequest h = new HttpRequest(UpdateFavoriteTeamsActivity.this, ServicesCall.TEAMS);
                    h.sendAuthenticatedPostRequest(getApplicationContext(), url);
                }
            });
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, StadiumActivity.class));
        finish();
    }
}
