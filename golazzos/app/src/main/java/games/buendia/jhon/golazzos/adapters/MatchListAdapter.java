package games.buendia.jhon.golazzos.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.activities.ConfirmacionJugadaActivity;
import games.buendia.jhon.golazzos.model.Match;
import games.buendia.jhon.golazzos.utils.ApplicationConstants;
import games.buendia.jhon.golazzos.utils.PreferencesHelper;

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
            holder.buttonPlayPoints = (Button) v.findViewById(R.id.buttonCincuentaPuntos);
            holder.textViewCienPuntos = (TextView) v.findViewById(R.id.textViewCienPuntos);
            holder.textViewHorasMinutosDias = (TextView) v.findViewById(R.id.textViewDiaHorasMinutos);
            holder.horaPartido = (TextView) v.findViewById(R.id.textViewHoraPartido);

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        final Match match = matchArrayList.get(position);

        holder.nombreEquipoLocal.setText(match.getLocalTeam().getTeamName());
        holder.nombreEquipoVisitante.setText(match.getAwayTeam().getTeamName());
        holder.jugarButton.setClickable(true);

        holder.buttonPlayPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.puntos_a_jugar))
                        .setSingleChoiceItems(ApplicationConstants.pointsToBet, 0, null)
                        .setPositiveButton(R.string.aceptar_button, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                if (selectedPosition >= 0) {
                                    if (selectedPosition != 0){
                                        holder.textViewCienPuntos.setText(context.getString(R.string.doscientos_puntos));
                                        holder.buttonPlayPoints.setText(context.getString(R.string.cien_puntos));
                                    }
                                    else {
                                        holder.textViewCienPuntos.setText(context.getString(R.string.cien_puntos));
                                        holder.buttonPlayPoints.setText(context.getString(R.string.cincuenta_puntos));
                                    }
                                    dialog.dismiss();
                                }
                            }
                        })
                        .show();
            }
        });

        holder.marcadorEquipoLocal.setClickable(true);
        holder.marcadorEquipoLocal.setFocusable(false);
        holder.marcadorEquipoLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NumberPicker myNumberPicker = new NumberPicker(context);
                myNumberPicker.setMaxValue(100);
                myNumberPicker.setMinValue(0);

                NumberPicker.OnValueChangeListener myValChangedListener = new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        holder.marcadorEquipoLocal.setText(String.valueOf(newVal));
                    }
                };

                myNumberPicker.setOnValueChangedListener(myValChangedListener);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle(context.getString(R.string.informacion));
                alertDialogBuilder.setMessage(context.getString(R.string.indica_marcador));

                alertDialogBuilder.setPositiveButton(context.getString(R.string.aceptar_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        holder.marcadorEquipoLocal.setText(String.valueOf(myNumberPicker.getValue()));
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setView(myNumberPicker);
                alertDialog.show();
            }
        });

        holder.marcadorEquipoVisitante.setClickable(true);
        holder.marcadorEquipoVisitante.setFocusable(false);
        holder.marcadorEquipoVisitante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NumberPicker myNumberPicker = new NumberPicker(context);
                myNumberPicker.setMaxValue(100);
                myNumberPicker.setMinValue(0);

                NumberPicker.OnValueChangeListener myValChangedListener = new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        holder.marcadorEquipoVisitante.setText(String.valueOf(newVal));
                    }
                };

                myNumberPicker.setOnValueChangedListener(myValChangedListener);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle(context.getString(R.string.informacion));
                alertDialogBuilder.setMessage(context.getString(R.string.indica_marcador));

                alertDialogBuilder.setPositiveButton(context.getString(R.string.aceptar_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        holder.marcadorEquipoVisitante.setText(String.valueOf(myNumberPicker.getValue()));
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setView(myNumberPicker);
                alertDialog.show();
            }
        });

        holder.jugarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ConfirmacionJugadaActivity.class);

                int pointsToBet = 0;

                if (holder.buttonPlayPoints.getText().toString().contains("50")){
                    pointsToBet = 50;
                }
                else pointsToBet = 100;

                if (!filterBet){
                    match.setMarcadorLocal(Integer.parseInt(holder.marcadorEquipoLocal.getText().toString()));
                    match.setMarcadorVisitante(Integer.parseInt(holder.marcadorEquipoVisitante.getText().toString()));
                    intent.putExtra("match", match);
                    intent.putExtra("opcion", context.getString(R.string.marcador));
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
                    intent = new Intent(context, ConfirmacionJugadaActivity.class);
                    intent.putExtra("match", match);
                    intent.putExtra("opcion", context.getString(R.string.gana_pierde));
                }

                if (PreferencesHelper.getUserPoints() - pointsToBet >= 0) {
                    intent.putExtra("pointsToBet", pointsToBet);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
                else {
                    Toast.makeText(context, context.getString(R.string.no_tienes_suficientes_puntos), Toast.LENGTH_SHORT).show();
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

        String stringHour = match.getTimeLabelMatch().split("T")[1];
        String stringDate = match.getTimeLabelMatch().split("T")[0];
        SimpleDateFormat formateador = new SimpleDateFormat(ApplicationConstants.formatDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat(ApplicationConstants.formatDateMatch);
        Date todayDate = new Date();

        try {

            Date dateMatch = formateador.parse(stringDate.replace("-","/")+" "+stringHour.substring(0, stringHour.length()-1));

            long diff = Math.abs(todayDate.getTime() - dateMatch.getTime());
            long daysDifference = Math.abs(Math.round(diff / (1000 * 3600 * 24)));

            long minDifference = diff / (60 * 1000) % 60;
            long hourDifference = diff / (60 * 60 * 1000) % 24;

            holder.textViewHorasMinutosDias.setText(String.format(context.getString(R.string.format_horas), String.valueOf(daysDifference), String.valueOf(hourDifference), String.valueOf(minDifference)));

            String dateMatchString = dateFormat.format(dateMatch.getTime());
            dateMatchString = dateMatchString.substring(0,1).toUpperCase() + dateMatchString.substring(1).toLowerCase();

            holder.horaPartido.setText(dateMatchString);


        } catch (ParseException e) {
            e.printStackTrace();
        }

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
        TextView textViewHorasMinutosDias;
        TextView resultadosSpinner;
        LinearLayout linearLayoutResultados;
        Button buttonPlayPoints;
        TextView textViewCienPuntos;
        TextView horaPartido;
    }
}
