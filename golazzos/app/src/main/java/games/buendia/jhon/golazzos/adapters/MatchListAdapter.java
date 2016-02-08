package games.buendia.jhon.golazzos.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import games.buendia.jhon.golazzos.R;
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

    public MatchListAdapter(ArrayList<Match> matchArrayList, Context context, ListView listViewMatches) {
        this.matchArrayList = matchArrayList;
        this.context = context;
        this.listViewMatches = listViewMatches;
        layoutInflater = LayoutInflater.from(context);
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

        ViewHolder holder;
        View v = listViewMatches.getChildAt(position - listViewMatches.getFirstVisiblePosition());

        if (v == null) {

            v = layoutInflater.inflate(R.layout.list_row_layout, null);
            holder = new ViewHolder();

            holder.nombreEquipoLocal = (TextView) v.findViewById(R.id.textViewNombreEquipoLocal);
            holder.nombreEquipoVisitante = (TextView) v.findViewById(R.id.textViewNombreEquipoVisitante);
            holder.imageEquipoLocal = (ImageView) v.findViewById(R.id.imageViewLocalTeam);
            holder.imageEquipoVisitante  = (ImageView) v.findViewById(R.id.imageViewAwayTeam);
            holder.spinnerPredicciones = (Spinner) v.findViewById(R.id.spinnerPronostico);
            holder.estadisticasCardView = (CardView) v.findViewById(R.id.cardViewEstadisticas);
            holder.progressBarEquipoLocal = (ProgressBar) v.findViewById(R.id.progressBarEquipoLocal);
            holder.progressBarEquipoVisitante = (ProgressBar) v.findViewById(R.id.progressBarEquipoVisitante);
            holder.nombreTorneoPartido = (TextView) v.findViewById(R.id.textViewNombreTorneo);

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        Match match = matchArrayList.get(position);

        holder.nombreEquipoLocal.setText(match.getLocalTeam().getCompleteTeamName());
        holder.nombreEquipoVisitante.setText(match.getAwayTeam().getCompleteTeamName());
        holder.spinnerPredicciones.setAdapter(new CustomSpinnerAdapter(context,ApplicationConstants.pronosticos));

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
        Spinner spinnerPredicciones;
        ImageView imageEquipoLocal;
        ImageView imageEquipoVisitante;
        CardView estadisticasCardView;
        ProgressBar progressBarEquipoLocal;
        ProgressBar progressBarEquipoVisitante;
        TextView nombreTorneoPartido;
    }
}
