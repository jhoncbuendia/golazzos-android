package games.buendia.jhon.golazzos.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;
import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.model.Match;
import games.buendia.jhon.golazzos.queryService.HttpRequest;
import games.buendia.jhon.golazzos.queryService.RequestInterface;
import games.buendia.jhon.golazzos.utils.DialogHelper;
import games.buendia.jhon.golazzos.utils.JSONBuilder;
import games.buendia.jhon.golazzos.utils.ServicesCall;

/**
 * Created by User on 13/02/2016.
 */
public class ConfirmacionJugadaActivity extends Activity implements RequestInterface {

    private Intent intent;
    private TextView tipoJugada, tipoVictoria, equipoLocal, equipoVisitante;
    private Match match;
    private int resourceTextVictoryType;
    private ImageView imageWinTeam;
    private ProgressBar progressBarImage;
    private CardView cardViewConfirmarJugada;
    private boolean isLocalWin = false, isAwayWin = false, isWithTypeBet = false;
    private int idBet = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacion_jugada);
        initUI();
    }

    private void initUI(){

        intent = getIntent();
        match = (Match) intent.getSerializableExtra("match");
        tipoJugada = (TextView) findViewById(R.id.textViewTipoJugada);
        tipoVictoria = (TextView) findViewById(R.id.textViewModoVictoria);
        equipoLocal = (TextView) findViewById(R.id.textViewIndicadorLocal);
        equipoVisitante = (TextView) findViewById(R.id.textViewEquipoVisitante);
        cardViewConfirmarJugada = (CardView) findViewById(R.id.cardViewConfirmarJugada);
        imageWinTeam = (ImageView) findViewById(R.id.imageViewEquipoGanador);
        progressBarImage = (ProgressBar) findViewById(R.id.progressBarLoaderImagen);
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
                DialogHelper.showLoaderDialog(ConfirmacionJugadaActivity.this);
                HttpRequest h = new HttpRequest(ConfirmacionJugadaActivity.this, null);
                JSONBuilder builderJson = new JSONBuilder();
                String url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.bet_endpoint));
                Log.i("json", builderJson.getBetJSON(match, isWithTypeBet, idBet).toString());
                h.startPostRequestAuthenticated(ConfirmacionJugadaActivity.this, url, builderJson.getBetJSON(match, isWithTypeBet, idBet),0);
            }
        });

    }

    @Override
    public void onSuccessCallBack(JSONObject response, ServicesCall serviceCall) {
        DialogHelper.hideLoaderDialog();
        startActivity(new Intent(this, MatchListActivity.class));
        finish();
    }

    @Override
    public void onErrorCallBack(JSONObject response) {

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MatchListActivity.class));
        finish();
    }
}
