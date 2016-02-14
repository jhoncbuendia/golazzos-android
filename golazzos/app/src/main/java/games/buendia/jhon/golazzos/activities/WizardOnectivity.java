package games.buendia.jhon.golazzos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import games.buendia.jhon.golazzos.utils.JSONBuilder;
import games.buendia.jhon.golazzos.utils.ServicesCall;

public class WizardOnectivity extends AppCompatActivity implements RequestInterface{

    private Spinner spinnerLigas;
    private Spinner spinnerEquipos;
    private Button buttonSiguiente;
    private ImageView imagenEquipo;
    private ProgressBar loaderImagen;
    private String url;
    private BuilderJsonList builderJsonList;
    private ArrayList<Team> teamsArrayList;
    private ArrayList<Tournament> tournamentArrayList;
    private boolean teamsLoaded = false, leaguesLoaded = false;
    private int idTournament;
    private boolean spinnerLigasPressed = false;
    private boolean spinnerEquiposPressed = false;
    private Team soulTeam;
    private int indexSelected = 0;
    private boolean ifIsSelected = false, findIt = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard_one);

        try {
            idTournament = getIntent().getIntExtra("tournament_id", 0);
        }
        catch (NullPointerException e){
            idTournament = 0;
        }

        DialogHelper.showLoaderDialog(WizardOnectivity.this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.tournaments_endpoint));
                HttpRequest h = new HttpRequest(WizardOnectivity.this, ServicesCall.LEAGUES);
                h.sendAuthenticatedPostRequest(getApplicationContext(), url);
            }
        });
    }

    private void initUI(){

        spinnerEquipos = (Spinner) findViewById(R.id.spinnerEquipos);
        spinnerLigas = (Spinner) findViewById(R.id.spinnerLigas);
        buttonSiguiente = (Button) findViewById(R.id.buttonSiguiente);
        loaderImagen = (ProgressBar) findViewById(R.id.progressBarLoaderImage);
        imagenEquipo = (ImageView) findViewById(R.id.imageViewTeam);

        boolean ifIsSelected = idTournament != 0, findIt = false;
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

        spinnerLigas.setAdapter(new CustomSpinnerAdapter(this, tournamentsStringArray));
        spinnerEquipos.setAdapter(new CustomSpinnerAdapter(this, teamsStringArray));

        spinnerEquipos.setFocusable(false);
        spinnerLigas.setFocusable(false);

        spinnerLigas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerLigasPressed) {
                    Intent intent = new Intent(WizardOnectivity.this, WizardOnectivity.class);
                    intent.putExtra("tournament_id", tournamentArrayList.get(i).getIdTournament());
                    startActivity(intent);
                    finish();
                }
                else spinnerLigasPressed = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        spinnerEquipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerEquiposPressed) {
                    soulTeam = teamsArrayList.get(i);
                    changeShirtUrl(teamsArrayList.get(i).getUrlTeam());
                } else spinnerEquiposPressed = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buttonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerEquiposPressed = false;
                spinnerLigasPressed = false;
                DialogHelper.showLoaderDialog(WizardOnectivity.this);
                url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.favorite_team_endpoint));
                JSONBuilder jsonBuilder = new JSONBuilder();
                HttpRequest h = new HttpRequest(WizardOnectivity.this, ServicesCall.FAVORITE_ADD);
                h.startPostRequestAuthenticated(getApplicationContext(), url, jsonBuilder.getFavoriteTeamJSON(String.valueOf(soulTeam.getIdTeam()), true),soulTeam.getIdTeam());
            }
        });

    }

    private void changeShirtUrl(String urlImage){

        loaderImagen.setVisibility(View.VISIBLE);

        if (!urlImage.contains("http"))
            urlImage = "http:"+urlImage;

        Picasso.with(this)
                .load(urlImage)
                .into(imagenEquipo, new Callback() {
                    @Override
                    public void onSuccess() {
                        loaderImagen.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError() {
                        loaderImagen.setVisibility(View.GONE);
                    }
                });

    }

    @Override
    public void onSuccessCallBack(JSONObject response, ServicesCall serviceCall) {
        builderJsonList = new BuilderJsonList(response);
        switch (serviceCall) {
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
                           HttpRequest h = new HttpRequest(WizardOnectivity.this, ServicesCall.TEAMS);
                           h.sendAuthenticatedPostRequest(getApplicationContext(), url);
                       }
                   });
               } catch (JSONException e) {
                   e.printStackTrace();
               }
               break;

            case FAVORITE_ADD:  DialogHelper.hideLoaderDialog();
                                startActivity(new Intent(WizardOnectivity.this, WizardTwoActivity.class));
                                finish();
                                break;
       }

        if (leaguesLoaded && teamsLoaded){
            initUI();
            DialogHelper.hideLoaderDialog();
        }
    }

    @Override
    public void onErrorCallBack(JSONObject response) {

    }
}