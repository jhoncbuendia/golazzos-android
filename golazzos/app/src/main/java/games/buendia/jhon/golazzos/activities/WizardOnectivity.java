package games.buendia.jhon.golazzos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.utils.ApplicationConstants;

public class WizardOnectivity extends AppCompatActivity {

    private Spinner spinnerLigas;
    private Spinner spinnerEquipos;
    private Button buttonSiguiente;
    private ImageView imagenEquipo;
    private ProgressBar loaderImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard_one);
        initUI();
    }

    private void initUI(){

        spinnerEquipos = (Spinner) findViewById(R.id.spinnerEquipos);
        spinnerLigas = (Spinner) findViewById(R.id.spinnerLigas);
        buttonSiguiente = (Button) findViewById(R.id.buttonSiguiente);
        loaderImagen = (ProgressBar) findViewById(R.id.progressBarLoaderImage);

        //TODO - pedir imagenes para usar en el momento que el usuario haga clic en el spinner
        //Por ahora se usaran referencias estaticas por cada valor del spinner de equipos (Imagen 139x139).

        imagenEquipo = (ImageView) findViewById(R.id.imageViewTeam);

        // TODO - Remover adapter harcodeados.

        spinnerLigas.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                                ApplicationConstants.ligas));

        spinnerLigas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {

                    case 1:
                        spinnerEquipos.setAdapter(new ArrayAdapter<String>(WizardOnectivity.this,
                                android.R.layout.simple_spinner_item,
                                ApplicationConstants.equiposLigaBBVA));
                        break;

                    case 2:
                        spinnerEquipos.setAdapter(new ArrayAdapter<String>(WizardOnectivity.this,
                                android.R.layout.simple_spinner_item,
                                ApplicationConstants.equiposSerieA));
                        break;

                    case 3:
                        spinnerEquipos.setAdapter(new ArrayAdapter<String>(WizardOnectivity.this,
                                android.R.layout.simple_spinner_item,
                                ApplicationConstants.equiposBundesliga));
                        break;

                    case 4:
                        spinnerEquipos.setAdapter(new ArrayAdapter<String>(WizardOnectivity.this,
                                android.R.layout.simple_spinner_item,
                                ApplicationConstants.equiposLigaAguila));
                        break;

                    case 5:
                        spinnerEquipos.setAdapter(new ArrayAdapter<String>(WizardOnectivity.this,
                                android.R.layout.simple_spinner_item,
                                ApplicationConstants.equiposPremier));
                        break;

                    default:
                        spinnerEquipos.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        spinnerEquipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                changeShirtUrl(spinnerLigas.getSelectedItemPosition(), i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buttonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WizardOnectivity.this, WizardTwoActivity.class));
            }
        });
    }

    private void changeShirtUrl(int indexLeague, int indexTeam){

        int indexArray = -1;

        if (indexTeam == 1) {
            switch (indexLeague) {
                case 1: indexArray = 0; break;
                case 2: indexArray = 1; break;
                case 3: indexArray = 2; break;
                case 4: indexArray = 3; break;
                case 5: indexArray = 4; break;
                default: indexArray = -1; break;
            }
        }

        if (indexArray != -1){

            loaderImagen.setVisibility(View.VISIBLE);

            Picasso.with(this)
                    .load(ApplicationConstants.urlsIcons.get(indexArray))
                    .into(imagenEquipo, new Callback() {
                        @Override
                        public void onSuccess() {
                            loaderImagen.setVisibility(View.GONE);
                        }
                        @Override
                        public void onError() {
                            loaderImagen.setVisibility(View.GONE);
                            imagenEquipo.setImageResource(R.drawable.elipse);
                        }
                    });
        }
        else {
            imagenEquipo.setImageResource(R.drawable.elipse);
        }
    }
}