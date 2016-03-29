package games.buendia.jhon.golazzos.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.model.Winner;

/**
 * Created by User on 29/03/2016.
 */
public class WeeklyWinnersAdapter extends BaseAdapter {

    private ArrayList<Winner> weeklyWinnesrArrayList;
    private Context context;
    private ListView listViewWinners;
    private LayoutInflater layoutInflater;
    private final int[] colorsArray = {Color.parseColor("#d7ff00"), Color.parseColor("#e8ff47"), Color.parseColor("#f0ff93"), Color.parseColor("#f4ffb4"), Color.parseColor("#f6fbd4")};

    public WeeklyWinnersAdapter(ArrayList<Winner> weeklyWinnesrArrayList, Context context, ListView listViewWinners) {
        this.weeklyWinnesrArrayList = weeklyWinnesrArrayList;
        this.context = context;
        this.listViewWinners = listViewWinners;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return weeklyWinnesrArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return weeklyWinnesrArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final ViewHolder holder;
        View v = listViewWinners.getChildAt(position - listViewWinners.getFirstVisiblePosition());

        if (v == null) {

            v = layoutInflater.inflate(R.layout.row_weekly_winners, null);
            holder = new ViewHolder();
            holder.textViewNumeroRanking = (TextView) v.findViewById(R.id.textViewNumeroRanking);
            holder.textViewPuntaje = (TextView) v.findViewById(R.id.textViewPuntaje);
            holder.textViewUserName = (TextView) v.findViewById(R.id.textViewUserName);
            holder.imageViewIconoUserProfile = (ImageView) v.findViewById(R.id.imageViewIconoUserProfile);
            holder.imageViewIconoUserSoulTeam = (ImageView) v.findViewById(R.id.imageViewIconoEquipoAlmaUser);
            holder.progressBarLoaderImageUserIcon = (ProgressBar) v.findViewById(R.id.progressBarLoaderUserImageIcon);
            holder.progressBarLoaderImageUserSoulTeam = (ProgressBar) v.findViewById(R.id.progressBarLoaderSoulTeamUser);
            holder.linearLayoutFondo = (LinearLayout) v.findViewById(R.id.layoutBackground);
            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        final Winner weeklyWinner = weeklyWinnesrArrayList.get(position);

        Picasso.with(context)
                .load(weeklyWinner.getUrlProfilePicture())
                .into(holder.imageViewIconoUserProfile, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBarLoaderImageUserIcon.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        holder.progressBarLoaderImageUserIcon.setVisibility(View.GONE);
                    }
                });

        String url = weeklyWinner.getUrlSoulTeamPicture();
        if (!url.contains("http"))
            url = "http:"+url;

        Picasso.with(context)
                .load(url)
                .into(holder.imageViewIconoUserSoulTeam, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBarLoaderImageUserSoulTeam.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        holder.progressBarLoaderImageUserSoulTeam.setVisibility(View.INVISIBLE);
                        holder.imageViewIconoUserSoulTeam.setVisibility(View.INVISIBLE);
                    }
                });

        holder.textViewPuntaje.setText(String.valueOf(weeklyWinner.getPointsSize()));
        holder.textViewNumeroRanking.setText(String.valueOf(weeklyWinner.getRank()));
        holder.textViewUserName.setText(weeklyWinner.getWinnerName()+"\n"+weeklyWinner.getWinnerSoulTeam());
        holder.linearLayoutFondo.setBackgroundColor(colorsArray[position]);

        return v;
    }

    static class ViewHolder {
        TextView textViewPuntaje;
        TextView textViewNumeroRanking;
        TextView textViewUserName;
        ImageView imageViewIconoUserProfile;
        ImageView imageViewIconoUserSoulTeam;
        ProgressBar progressBarLoaderImageUserIcon;
        ProgressBar progressBarLoaderImageUserSoulTeam;
        LinearLayout linearLayoutFondo;
    }
}
