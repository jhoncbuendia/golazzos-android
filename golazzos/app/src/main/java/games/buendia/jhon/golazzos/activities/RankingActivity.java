package games.buendia.jhon.golazzos.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.utils.PreferencesHelper;

/**
 * Created by User on 13/03/2016.
 */
public class RankingActivity extends Activity {

    private final int[] graphResource = {R.id.firstBar, R.id.secondBar, R.id.thirdBar,
            R.id.fourBar, R.id.fiveBar, R.id.sixBar, R.id.sevenBar,
            R.id.eightBar, R.id.nineBar};
    private ImageView imageViewLogoNivel;
    private TextView textViewNivelNumeroTexto, textViewNivelNombre, textViewAciertos, textViewRecompensa;
    private ProgressBar progressBarLoaderImagen;
    private ImageButton imageButtonBack;
    private LinearLayout layoutGanadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        imageViewLogoNivel = (ImageView) findViewById(R.id.imageViewLogoNivel);
        textViewNivelNumeroTexto = (TextView) findViewById(R.id.textViewNivelUsuarioNumeroYLetras);
        textViewNivelNombre = (TextView) findViewById(R.id.textViewNivelNombre);
        textViewAciertos = (TextView) findViewById(R.id.textViewUserHits);
        textViewRecompensa = (TextView) findViewById(R.id.textViewRecompensa);
        progressBarLoaderImagen = (ProgressBar) findViewById(R.id.progressBarLoaderImagen);
        imageButtonBack = (ImageButton) findViewById(R.id.imageButtonBackArrow);
        layoutGanadores = (LinearLayout) findViewById(R.id.linearLayoutGanadores);

        layoutGanadores.setClickable(true);
        layoutGanadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RankingActivity.this, WeeklyAwardsActivity.class));
                finish();
            }
        });

        progressBarLoaderImagen.setVisibility(View.VISIBLE);

        Picasso.with(this)
                .load(PreferencesHelper.getLevelImageUrl())
                .into(imageViewLogoNivel, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBarLoaderImagen.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        progressBarLoaderImagen.setVisibility(View.GONE);
                        imageViewLogoNivel.setImageResource(R.drawable.default_icon_level);
                    }
                });

        textViewAciertos.setText(String.format(getString(R.string.puntos_format), String.valueOf(PreferencesHelper.getHitsCountLevelUser())));
        textViewNivelNombre.setText(PreferencesHelper.getUserNameLevel());
        textViewNivelNumeroTexto.setText(String.format(getString(R.string.nivel_number_name_format), String.valueOf(PreferencesHelper.getUserLevelNumber()), PreferencesHelper.getUserNameLevel()));
        textViewRecompensa.setText(String.format(getString(R.string.recompensa_format), String.valueOf(PreferencesHelper.getUserPointsTrophy())));

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        initGraph(PreferencesHelper.getUserLevelNumber());
    }

    private void initGraph(int level){

        for (int i = 0; i < level; i++){
            findViewById(graphResource[i]).setBackgroundColor(Color.parseColor("#d6fe00"));
        }

        for (int i = level; i < graphResource.length; i++){
            findViewById(graphResource[i]).setBackgroundColor(Color.parseColor("#0d597f"));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MatchListActivity.class));
        finish();
    }
}
