package games.buendia.jhon.golazzos.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.activities.ConfirmacionJugadaActivity;
import games.buendia.jhon.golazzos.model.Match;
import games.buendia.jhon.golazzos.utils.ApplicationConstants;

/**
 * Created by User on 07/02/2016.
 */
public class MatchListAdapter extends BaseAdapter {

    private ArrayList<Match> matchArrayList;
    private Context context;
    private ListView listViewMatches;
    private LayoutInflater layoutInflater;
    private boolean filterBet;

    public MatchListAdapter(ArrayList<Match> matchArrayList, Context context, ListView listViewMatches, boolean filterBet) {
        this.matchArrayList = matchArrayList;
        this.context = context;
        this.listViewMatches = listViewMatches;
        layoutInflater = LayoutInflater.from(context);
        this.filterBet = filterBet;
    }

    @Override
    public int getCount() {
        return matchArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return matchArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final ViewHolder holder;
        View v = listViewMatches.getChildAt(position - listViewMatches.getFirstVisiblePosition());

        if (v == null) {

            v = layoutInflater.inflate(R.layout.list_row_layout, null);
            holder = new ViewHolder();

            holder.nombreEquipoLocal = (TextView) v.findViewById(R.id.textViewNombreEquipoLocal);
            holder.nombreEquipoVisitante = (TextView) v.findViewById(R.id.textViewNombreEquipoVisitante);
            holder.imageEquipoLocal = (ImageView) v.findViewById(R.id.imageViewLocalTeam);
            holder.imageEquipoVisitante  = (ImageView) v.findViewById(R.id.imageViewAwayTeam);
            holder.estadisticasCardView = (CardView) v.findViewById(R.id.cardViewEstadisticas);
            holder.progressBarEquipoLocal = (ProgressBar) v.findViewById(R.id.progressBarEquipoLocal);
            holder.progressBarEquipoVisitante = (ProgressBar) v.findViewById(R.id.progressBarEquipoVisitante);
            holder.nombreTorneoPartido = (TextView) v.findViewById(R.id.textViewNombreTorneo);
            holder.marcadorEquipoLocal = (TextView) v.findViewById(R.id.editTextMarcadorLocal);
            holder.marcadorEquipoVisitante = (TextView) v.findViewById(R.id.editTextMarcadorVisitante);
            holder.jugarButton = (CardView) v.findViewById(R.id.cardViewJugar);
            holder.resultadosSpinner = (TextView) v.findViewById(R.id.spinnerPronostico);
            holder.linearLayoutResultados = (LinearLayout) v.findViewById(R.id.marcadorLayout);

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        final Match match = matchArrayList.get(position);

        holder.nombreEquipoLocal.setText(match.getLocalTeam().getTeamName());
        holder.nombreEquipoVisitante.setText(match.getAwayTeam().getTeamName());
        holder.jugarButton.setClickable(true);

        holder.marcadorEquipoLocal.setClickable(true);
        holder.marcadorEquipoLocal.setFocusable(false);
        holder.marcadorEquipoLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NumberPicker myNumberPicker = new NumberPicker(context);
                myNumberPicker.setMaxValue(100);
                myNumberPicker.setMinValue(0);

                NumberPicker.OnValueChangeListener myValChangedListener = new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        holder.marcadorEquipoLocal.setText(String.valueOf(newVal));
                    }
                };

                myNumberPicker.setOnValueChangedListener(myValChangedListener);

                new AlertDialog.Builder(context).setView(myNumberPicker).show();
            }
        });

        holder.marcadorEquipoVisitante.setClickable(true);
        holder.marcadorEquipoVisitante.setFocusable(false);
        holder.marcadorEquipoVisitante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NumberPicker myNumberPicker = new NumberPicker(context);
                myNumberPicker.setMaxValue(100);
                myNumberPicker.setMinValue(0);

                NumberPicker.OnValueChangeListener myValChangedListener = new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        holder.marcadorEquipoVisitante.setText(String.valueOf(newVal));
                    }
                };

                myNumberPicker.setOnValueChangedListener(myValChangedListener);

                new AlertDialog.Builder(context).setView(myNumberPicker).show();
            }
        });

        holder.jugarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!filterBet){
                    match.setMarcadorLocal(Integer.parseInt(holder.marcadorEquipoLocal.getText().toString()));
                    match.setMarcadorVisitante(Integer.parseInt(holder.marcadorEquipoVisitante.getText().toString()));
                    Intent intent = new Intent(context, ConfirmacionJugadaActivity.class);
                    intent.putExtra("match", match);
                    intent.putExtra("opcion", context.getString(R.string.marcador));
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
                else {
                    if (holder.resultadosSpinner.getText().toString().equals(context.getString(R.string.gana_visitante))) {
                        match.setMarcadorLocal(0);
                        match.setMarcadorVisitante(1);
                    }
                    else if (holder.resultadosSpinner.getText().toString().equals(context.getString(R.string.gana_local))) {
                        match.setMarcadorLocal(1);
                        match.setMarcadorVisitante(0);
                    }
                    else {
                        match.setMarcadorLocal(0);
                        match.setMarcadorVisitante(0);
                    }
                    Intent intent = new Intent(context, ConfirmacionJugadaActivity.class);
                    intent.putExtra("match", match);
                    intent.putExtra("opcion", context.getString(R.string.gana_pierde));
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        });


        if (filterBet){
            holder.linearLayoutResultados.setVisibility(View.VISIBLE);
            holder.marcadorEquipoLocal.setVisibility(View.GONE);
            holder.marcadorEquipoVisitante.setVisibility(View.GONE);
            holder.resultadosSpinner.setClickable(true);
            holder.resultadosSpinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(context)
                            .setTitle(context.getString(R.string.titulo_alerta))
                            .setSingleChoiceItems(ApplicationConstants.pronosticos, 0, null)
                            .setPositiveButton(R.string.aceptar_button, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                    if (selectedPosition >= 0) {
                                        dialog.dismiss();
                                        holder.resultadosSpinner.setText(ApplicationConstants.pronosticos[selectedPosition]);
                                    }
                                }
                            })
                            .show();
                }
            });
        }
        else {
            holder.linearLayoutResultados.setVisibility(View.GONE);
            holder.marcadorEquipoLocal.setVisibility(View.VISIBLE);
            holder.marcadorEquipoVisitante.setVisibility(View.VISIBLE);
        }

        holder.nombreTorneoPartido.setText(match.getLocalTeam().getTournamentTeam().getNameTornament());

        final ProgressBar equipoLocalProgressBar = holder.progressBarEquipoLocal;
        final ProgressBar equipoVisitanteProgressBar = holder.progressBarEquipoVisitante;

        Picasso.with(context)
                .load("http:" + match.getLocalTeam().getUrlTeam())
                .into(holder.imageEquipoLocal, new Callback() {
                    @Override
                    public void onSuccess() {
                        equipoLocalProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        equipoLocalProgressBar.setVisibility(View.GONE);
                    }
                });

        Picasso.with(context)
                .load("http:" + match.getAwayTeam().getUrlTeam())
                .into(holder.imageEquipoVisitante, new Callback() {
                    @Override
                    public void onSuccess() {
                        equipoVisitanteProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        equipoVisitanteProgressBar.setVisibility(View.GONE);
                    }
                });

        return v;
    }

    static class ViewHolder {
        TextView nombreEquipoLocal;
        TextView nombreEquipoVisitante;
        TextView marcadorEquipoLocal;
        TextView marcadorEquipoVisitante;
        ImageView imageEquipoLocal;
        ImageView imageEquipoVisitante;
        CardView estadisticasCardView;
        ProgressBar progressBarEquipoLocal;
        ProgressBar progressBarEquipoVisitante;
        TextView nombreTorneoPartido;
        CardView jugarButton;
        TextView resultadosSpinner;
        LinearLayout linearLayoutResultados;
    }
}
