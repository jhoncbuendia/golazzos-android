package games.buendia.jhon.golazzos.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;
import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.model.Match;
import games.buendia.jhon.golazzos.queryService.HttpRequest;
import games.buendia.jhon.golazzos.queryService.RequestInterface;
import games.buendia.jhon.golazzos.utils.DialogHelper;
import games.buendia.jhon.golazzos.utils.JSONBuilder;
import games.buendia.jhon.golazzos.utils.PreferencesHelper;
import games.buendia.jhon.golazzos.utils.ServicesCall;

/**
 * Created by User on 13/02/2016.
 */
public class ConfirmacionJugadaActivity extends Activity implements RequestInterface {

    private Intent intent;
    private TextView tipoJugada, tipoVictoria, equipoLocal, equipoVisitante, marcadorLocal, marcadorVisitante;
    private Match match;
    private int resourceTextVictoryType;
    private ImageView imageWinTeam;
    private ProgressBar progressBarImage;
    private CardView cardViewConfirmarJugada;
    private boolean isLocalWin = false, isAwayWin = false, isWithTypeBet = false;
    private String intentFilterStringTeam = "";
    private int idTournament = 0;
    private int idBet = 0, pointsToBet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacion_jugada);
        initUI();
    }

    private void initUI(){

        intent = getIntent();

        intentFilterStringTeam = intent.getStringExtra("team_name");
        idTournament = intent.getIntExtra("tournament_id", 0);

        match = (Match) intent.getSerializableExtra("match");
        pointsToBet = intent.getIntExtra("pointsToBet",0);
        tipoJugada = (TextView) findViewById(R.id.textViewTipoJugada);
        tipoVictoria = (TextView) findViewById(R.id.textViewModoVictoria);
        equipoLocal = (TextView) findViewById(R.id.textViewIndicadorLocal);
        equipoVisitante = (TextView) findViewById(R.id.textViewEquipoVisitante);
        cardViewConfirmarJugada = (CardView) findViewById(R.id.cardViewConfirmarJugada);
        imageWinTeam = (ImageView) findViewById(R.id.imageViewEquipoGanador);
        progressBarImage = (ProgressBar) findViewById(R.id.progressBarLoaderImagen);
        marcadorLocal = (TextView) findViewById(R.id.textViewMarcadorLocal);
        marcadorVisitante = (TextView) findViewById(R.id.textViewMarcadorVisitante);
        cardViewConfirmarJugada.setClickable(true);

        equipoLocal.setText(match.getLocalTeam().getTeamName());
        equipoVisitante.setText(match.getAwayTeam().getTeamName());

        if (match.getMarcadorLocal() == match.getMarcadorVisitante()){
            idBet = 3;
            resourceTextVictoryType = R.string.empate;
        }
        else if (match.getMarcadorLocal() > match.getMarcadorVisitante()){
            isLocalWin = true;
            idBet = 1;
            resourceTextVictoryType = R.string.gana_local;
        }
        else {
            isAwayWin = true;
            idBet = 2;
            resourceTextVictoryType = R.string.gana_visitante;
        }

        tipoVictoria.setText(resourceTextVictoryType);

        if (intent.getStringExtra("opcion").contains(getString(R.string.gana_pierde))){
            isWithTypeBet = true;
            tipoJugada.setText(R.string.gana_pierde);
        }
        else {
            tipoJugada.setText(R.string.marcador);
            marcadorLocal.setText(String.valueOf(match.getMarcadorLocal()));
            marcadorVisitante.setText(String.valueOf(match.getMarcadorVisitante()));
            marcadorLocal.setVisibility(View.VISIBLE);
            marcadorVisitante.setVisibility(View.VISIBLE);
        }

        if (isLocalWin){
            Picasso.with(this)
                    .load("http:" + match.getLocalTeam().getUrlTeam())
                    .into(imageWinTeam, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBarImage.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            progressBarImage.setVisibility(View.GONE);
                        }
                    });
        }
        else if (isAwayWin){
            Picasso.with(this)
                    .load("http:" + match.getAwayTeam().getUrlTeam())
                    .into(imageWinTeam, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBarImage.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            progressBarImage.setVisibility(View.GONE);
                        }
                    });
        }
        else {
            progressBarImage.setVisibility(View.GONE);
        }

        cardViewConfirmarJugada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PreferencesHelper.getUserPoints() - pointsToBet >= 0) {
                    DialogHelper.showLoaderDialog(ConfirmacionJugadaActivity.this);
                    HttpRequest h = new HttpRequest(ConfirmacionJugadaActivity.this, null);
                    JSONBuilder builderJson = new JSONBuilder();
                    String url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.bet_endpoint));
                    Log.i("json-send", builderJson.getBetJSON(match, isWithTypeBet, idBet, pointsToBet * 100).toString());
                    h.startPostRequestAuthenticated(ConfirmacionJugadaActivity.this, url, builderJson.getBetJSON(match, isWithTypeBet, idBet, pointsToBet * 100), 0);
                }
            }

        });

    }

    @Override
    public void onSuccessCallBack(JSONObject response, ServicesCall serviceCall) {
        DialogHelper.hideLoaderDialog();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getString(R.string.operacion_exitosa));
        alertDialogBuilder.setMessage(getString(R.string.apuesta_exitosa));

        alertDialogBuilder.setPositiveButton(getString(R.string.aceptar_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                PreferencesHelper.updateUserPoints(PreferencesHelper.getUserPoints() - pointsToBet);
                onBackPressed();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onErrorCallBack(JSONObject response) {
        DialogHelper.hideLoaderDialog();
        Toast.makeText(ConfirmacionJugadaActivity.this, getString(R.string.error_transaccional), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intentBack = new Intent(this, MatchListActivity.class);
        intentBack.putExtra("filter_bet", isWithTypeBet);
        intentBack.putExtra("team_name", intentFilterStringTeam);
        intentBack.putExtra("tournament_id", idTournament);
        intentBack.putExtra("filter_bet", isWithTypeBet);
        startActivity(intentBack);
        finish();
    }
}