package games.buendia.jhon.golazzos.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.model.Team;
import games.buendia.jhon.golazzos.utils.ApplicationConstants;

public class WizardTwoActivity extends AppCompatActivity {

    private Spinner spinnerLigas;
    private Spinner spinnerEquipos;
    private Button buttonSiguiente;
    private Button buttonAgregar;
    private ArrayList<Team> teamArrayList;
    private Drawable elipseBlanca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard_two);
        teamArrayList = new ArrayList<Team>();
        elipseBlanca = getResources().getDrawable(R.drawable.elipse_blanca);
        initUI();
    }

    private void initUI() {

        spinnerEquipos = (Spinner) findViewById(R.id.spinnerEquipos);
        spinnerLigas = (Spinner) findViewById(R.id.spinnerLigas);
        buttonSiguiente = (Button) findViewById(R.id.buttonSiguiente);
        buttonAgregar = (Button) findViewById(R.id.buttonAgregarEquipo);

        // TODO - Remover adapter harcodeados.

        spinnerLigas.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                ApplicationConstants.ligas));

        spinnerLigas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {

                    case 1:
                        spinnerEquipos.setAdapter(new ArrayAdapter<String>(WizardTwoActivity.this,
                                android.R.layout.simple_spinner_item,
                                ApplicationConstants.equiposLigaBBVA));
                        break;

                    case 2:
                        spinnerEquipos.setAdapter(new ArrayAdapter<String>(WizardTwoActivity.this,
                                android.R.layout.simple_spinner_item,
                                ApplicationConstants.equiposSerieA));
                        break;

                    case 3:
                        spinnerEquipos.setAdapter(new ArrayAdapter<String>(WizardTwoActivity.this,
                                android.R.layout.simple_spinner_item,
                                ApplicationConstants.equiposBundesliga));
                        break;

                    case 4:
                        spinnerEquipos.setAdapter(new ArrayAdapter<String>(WizardTwoActivity.this,
                                android.R.layout.simple_spinner_item,
                                ApplicationConstants.equiposLigaAguila));
                        break;

                    case 5:
                        spinnerEquipos.setAdapter(new ArrayAdapter<String>(WizardTwoActivity.this,
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

        buttonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WizardTwoActivity.this, WizardThreeActivity.class));
            }
        });

        buttonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerLigas.getSelectedItemPosition() != 0) {
                    if (spinnerEquipos.getSelectedItemPosition() != 0) {
                        if (!isInTeamList(spinnerEquipos.getSelectedItem().toString())) {
                            addInRowLayout(spinnerEquipos.getSelectedItem().toString(), teamArrayList.size());
                        } else {
                            Toast.makeText(WizardTwoActivity.this, getString(R.string.equipo_agregado), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(WizardTwoActivity.this, getString(R.string.debes_seleccionar_un_equipo), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(WizardTwoActivity.this, getString(R.string.debes_seleccionar_una_liga), Toast.LENGTH_SHORT).show();
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

    private void addInRowLayout(String teamName, int sizeList){
        switch (sizeList){

            case 1: ((TextView) findViewById(R.id.textViewFirstTeam)).setText(teamName);
                    ((TextView) findViewById(R.id.textViewFirstTeam)).setCompoundDrawablesWithIntrinsicBounds(null, elipseBlanca, null, null);
                    break;

            case 2: ((TextView) findViewById(R.id.textViewSecondTeam)).setText(teamName);
                    ((TextView) findViewById(R.id.textViewSecondTeam)).setCompoundDrawablesWithIntrinsicBounds(null, elipseBlanca,null,null);
                    break;

            case 3: ((TextView) findViewById(R.id.textViewThirdTeam)).setText(teamName);
                    ((TextView) findViewById(R.id.textViewThirdTeam)).setCompoundDrawablesWithIntrinsicBounds(null, elipseBlanca, null, null);
                    break;

            case 4: ((TextView) findViewById(R.id.textViewFourTeam)).setText(teamName);
                    ((TextView) findViewById(R.id.textViewFourTeam)).setCompoundDrawablesWithIntrinsicBounds(null, elipseBlanca, null, null);
                    break;

            case 5: ((TextView) findViewById(R.id.textViewFifthTeam)).setText(teamName);
                    ((TextView) findViewById(R.id.textViewFifthTeam)).setCompoundDrawablesWithIntrinsicBounds(null, elipseBlanca, null, null);
                    break;

            case 6: ((TextView) findViewById(R.id.textViewSixTeam)).setText(teamName);
                    ((TextView) findViewById(R.id.textViewSixTeam)).setCompoundDrawablesWithIntrinsicBounds(null, elipseBlanca, null, null);
                    break;

            case 7: ((TextView) findViewById(R.id.textViewSevenTeam)).setText(teamName);
                    ((TextView) findViewById(R.id.textViewSevenTeam)).setCompoundDrawablesWithIntrinsicBounds(null, elipseBlanca, null, null);
                    break;

            case 8: ((TextView) findViewById(R.id.textViewEightTeam)).setText(teamName);
                    ((TextView) findViewById(R.id.textViewEightTeam)).setCompoundDrawablesWithIntrinsicBounds(null,elipseBlanca,null,null);
                    break;

            case 9: Toast.makeText(this, getString(R.string.equipos_agregados_completos),Toast.LENGTH_SHORT).show(); break;
        }
    }

}