package games.buendia.jhon.golazzos.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.adapters.CustomSpinnerAdapter;
import games.buendia.jhon.golazzos.model.Team;
import games.buendia.jhon.golazzos.model.Tournament;
import games.buendia.jhon.golazzos.queryService.BuilderJsonList;
import games.buendia.jhon.golazzos.queryService.HttpRequest;
import games.buendia.jhon.golazzos.queryService.RequestInterface;
import games.buendia.jhon.golazzos.utils.DialogHelper;
import games.buendia.jhon.golazzos.utils.ServicesCall;

public class WizardTwoActivity extends Activity implements RequestInterface, AdapterView.OnItemSelectedListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard_two);
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

        DialogHelper.showLoaderDialog(WizardTwoActivity.this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.tournaments_endpoint));
                HttpRequest h = new HttpRequest(WizardTwoActivity.this, ServicesCall.LEAGUES);
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
                startActivity(new Intent(WizardTwoActivity.this, WizardThreeActivity.class));
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
                    Toast.makeText(WizardTwoActivity.this, getString(R.string.equipo_agregado), Toast.LENGTH_SHORT).show();
                }
            }
        });

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

        Log.i("url =>", url);

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
                            HttpRequest h = new HttpRequest(WizardTwoActivity.this, ServicesCall.TEAMS);
                            h.sendAuthenticatedPostRequest(getApplicationContext(), url);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
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
    }

    @Override
    public void onErrorCallBack(JSONObject response) {

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
                    HttpRequest h = new HttpRequest(WizardTwoActivity.this, ServicesCall.TEAMS);
                    h.sendAuthenticatedPostRequest(getApplicationContext(), url);
                }
            });
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}