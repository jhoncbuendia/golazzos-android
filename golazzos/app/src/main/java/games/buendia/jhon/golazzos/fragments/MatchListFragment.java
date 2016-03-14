package games.buendia.jhon.golazzos.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.daimajia.swipe.SwipeLayout;
import java.util.ArrayList;
import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.activities.EnVivoMatchActivity;
import games.buendia.jhon.golazzos.activities.FinalizadosMatchActivity;
import games.buendia.jhon.golazzos.activities.MatchListActivity;
import games.buendia.jhon.golazzos.activities.RankingActivity;
import games.buendia.jhon.golazzos.activities.StadiumActivity;
import games.buendia.jhon.golazzos.activities.UpdateFavoriteTeamsActivity;
import games.buendia.jhon.golazzos.activities.UpdateSoulTeamActivity;
import games.buendia.jhon.golazzos.adapters.CustomSpinnerAdapter;
import games.buendia.jhon.golazzos.adapters.MatchListAdapter;
import games.buendia.jhon.golazzos.model.Match;
import games.buendia.jhon.golazzos.model.Team;
import games.buendia.jhon.golazzos.model.Tournament;
import games.buendia.jhon.golazzos.utils.ApplicationConstants;

/**
 * Created by User on 07/02/2016.
 */
public class MatchListFragment extends Fragment {

    private boolean spinnerLigasPressed = false;
    private boolean spinnerEquiposPressed = false;
    private boolean filterBet = false;
    private ViewPager viewPagerMatches;
    private TextView textViewPorJugar, textViewEnVivo, textViewFinalizado;
    private Class activityToRefresh;
    private SwipeLayout swipeLayout;
    private LinearLayout startSlide, endSlide;

    public MatchListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_list_match, container, false);

        final Bundle arguments = getArguments();

        ArrayList<Match> matches = (ArrayList<Match>) arguments.getSerializable("matches");
        final ArrayList<Tournament> tournaments = (ArrayList<Tournament>) arguments.getSerializable("leagues");
        final ArrayList<Team> teams = (ArrayList<Team>) arguments.getSerializable("teams");

        filterBet = arguments.getBoolean("filter_beat");

        viewPagerMatches = (ViewPager) view.findViewById(R.id.viewPagerMatches);
        viewPagerMatches.setAdapter(new MyPagerAdapter(matches));
        viewPagerMatches.setCurrentItem(0);

        textViewPorJugar = (TextView) view.findViewById(R.id.textViewPorJugar);
        textViewEnVivo = (TextView) view.findViewById(R.id.textViewEnVivo);
        textViewFinalizado = (TextView) view.findViewById(R.id.textViewFinalizado);
        startSlide = (LinearLayout) view.findViewById(R.id.slide_start);
        endSlide = (LinearLayout) view.findViewById(R.id.slide_end);

        textViewFinalizado.setClickable(true);
        textViewEnVivo.setClickable(true);
        textViewPorJugar.setClickable(true);

        int indexSelected = 0;
        String[] tournamentsStringArray = new String[tournaments.size()+1];
        tournamentsStringArray[0] = getActivity().getString(R.string.drop_down_ligas);
        final int idTournament = arguments.getInt("tournament_id");
        boolean ifIsSelected = idTournament != 0, findIt = false;

        for (int i = 0; i < tournaments.size(); i++) {
            tournamentsStringArray[i+1] = tournaments.get(i).getNameTornament();
            if (ifIsSelected){
                if (!findIt) {
                    if (tournaments.get(i).getIdTournament() == idTournament){
                        indexSelected = i;
                        findIt = true;
                    }
                }
            }
        }

        if (ifIsSelected){
            tournamentsStringArray[indexSelected+1] = getActivity().getString(R.string.drop_down_ligas);
            tournamentsStringArray[0] = tournaments.get(indexSelected).getNameTornament();
        }

        String teamName = getArguments().getString("teamName");
        String[] teamsStringArray = new String[teams.size()+1];
        teamsStringArray[0] = getActivity().getString(R.string.drop_down_equipos);
        boolean ifIsSelectedTeam = !teamName.isEmpty(), findItTeam = false;
        int indexTeamSelected = 0;

        for (int i = 0; i < teams.size(); i++) {
            teamsStringArray[i+1] = teams.get(i).getTeamName();
            if (ifIsSelectedTeam){
                if (!findItTeam) {
                    if (teams.get(i).getTeamName().equals(teamName)){
                        indexTeamSelected = i;
                        findItTeam = true;
                    }
                }
            }
        }

        if (ifIsSelectedTeam){
            teamsStringArray[indexTeamSelected+1] = getActivity().getString(R.string.drop_down_equipos);
            teamsStringArray[0] = teams.get(indexTeamSelected).getTeamName();
        }


        final Spinner spinnerLigas = (Spinner) view.findViewById(R.id.spinnerLigas);
        final Spinner spinnerEquipos = (Spinner) view.findViewById(R.id.spinnerEquipos);

        spinnerLigas.setAdapter(new CustomSpinnerAdapter(getActivity(), tournamentsStringArray));
        spinnerEquipos.setAdapter(new CustomSpinnerAdapter(getActivity(), teamsStringArray));

        spinnerEquipos.setFocusable(false);
        spinnerLigas.setFocusable(false);


        textViewPorJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MatchListActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        textViewEnVivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EnVivoMatchActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        textViewFinalizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FinalizadosMatchActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        if (getActivity() instanceof MatchListActivity){
            activityToRefresh = MatchListActivity.class;
            checkCurrentItem(0);
        }
        else if (getActivity() instanceof EnVivoMatchActivity) {
            activityToRefresh = EnVivoMatchActivity.class;
            checkCurrentItem(1);
        }
        else if (getActivity() instanceof FinalizadosMatchActivity){
            activityToRefresh = FinalizadosMatchActivity.class;
            checkCurrentItem(2);
        }

        swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        if (!filterBet) {
            swipeLayout.setDragEdge(SwipeLayout.DragEdge.Left);
        }
        else {
            swipeLayout.setDragEdge(SwipeLayout.DragEdge.Right);
            startSlide.setBackgroundResource(R.drawable.slide_banner);
            endSlide.setBackgroundResource(R.drawable.banner_marcador);
        }

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {

            @Override
            public void onStartOpen(SwipeLayout swipeLayout) {

            }

            @Override
            public void onOpen(SwipeLayout swipeLayout) {
                Intent intent = new Intent(getActivity(), activityToRefresh);
                intent.putExtra("tournament_id", idTournament);
                intent.putExtra("filter_bet", !filterBet);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onStartClose(SwipeLayout swipeLayout) {

            }

            @Override
            public void onClose(SwipeLayout swipeLayout) {
                Intent intent = new Intent(getActivity(), activityToRefresh);
                intent.putExtra("tournament_id", idTournament);
                intent.putExtra("filter_bet", !filterBet);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onUpdate(SwipeLayout swipeLayout, int i, int i2) {

            }

            @Override
            public void onHandRelease(SwipeLayout swipeLayout, float v, float v2) {

            }

        });


        view.findViewById(R.id.imageButtonHamburguerMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MatchListActivity){
                    ((MatchListActivity) getActivity()).openDrawerMenu(view);
                }
                else if (getActivity() instanceof EnVivoMatchActivity) {
                    ((EnVivoMatchActivity) getActivity()).openDrawerMenu(view);
                }
                else if (getActivity() instanceof FinalizadosMatchActivity){
                    ((FinalizadosMatchActivity) getActivity()).openDrawerMenu(view);
                }

            }
        });

        spinnerLigas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerLigasPressed) {
                    if (i != 0) {
                        Intent intent = new Intent(getActivity(), activityToRefresh);
                        intent.putExtra("tournament_id", tournaments.get(i-1).getIdTournament());
                        startActivity(intent);
                        getActivity().finish();
                    }
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
                   if (i != 0) {
                       Intent intent = new Intent(getActivity(), activityToRefresh);
                       intent.putExtra("team_name", teams.get(i-1).getTeamName());
                       intent.putExtra("tournament_id", arguments.getInt("tournament_id"));
                       startActivity(intent);
                       getActivity().finish();
                   }
               }
               else spinnerEquiposPressed = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        LinearLayout layoutFanaticada = (LinearLayout) view.findViewById(R.id.layoutFanaticada);
        LinearLayout layoutFavoritos = (LinearLayout) view.findViewById(R.id.layoutFavoritos);
        LinearLayout layoutPartidos = (LinearLayout) view.findViewById(R.id.layoutPartidos);
        LinearLayout layoutRanking = (LinearLayout) view.findViewById(R.id.layoutRanking);
        LinearLayout layoutAmigos = (LinearLayout) view.findViewById(R.id.layoutAmigos);

        layoutFavoritos.setClickable(true);
        layoutFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.selecciona_opcion));
                builder.setItems(ApplicationConstants.opcionesAlerta, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            startActivity(new Intent(getActivity(), UpdateSoulTeamActivity.class));
                            getActivity().finish();
                        } else {
                            startActivity(new Intent(getActivity(), UpdateFavoriteTeamsActivity.class));
                            getActivity().finish();
                        }
                    }
                });
                builder.show();
            }
        });

        layoutFanaticada.setClickable(true);
        layoutFanaticada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), StadiumActivity.class));
                getActivity().finish();
            }
        });

        layoutRanking.setClickable(true);
        layoutRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RankingActivity.class));
                getActivity().finish();
            }
        });


        return view;
    }

    public void checkCurrentItem(int position){
        switch (position){
            case 0: textViewPorJugar.setBackgroundColor(Color.parseColor("#323232"));
                textViewPorJugar.setTextColor(Color.parseColor("#FFFFFF"));
                textViewFinalizado.setBackgroundColor(Color.parseColor("#FFFFFF"));
                textViewEnVivo.setBackgroundColor(Color.parseColor("#FFFFFF"));
                textViewFinalizado.setTextColor(Color.parseColor("#005b7d"));
                textViewEnVivo.setTextColor(Color.parseColor("#005b7d"));
                break;

            case 1: textViewEnVivo.setBackgroundColor(Color.parseColor("#323232"));
                textViewEnVivo.setTextColor(Color.parseColor("#FFFFFF"));
                textViewFinalizado.setBackgroundColor(Color.parseColor("#FFFFFF"));
                textViewPorJugar.setBackgroundColor(Color.parseColor("#FFFFFF"));
                textViewFinalizado.setTextColor(Color.parseColor("#005b7d"));
                textViewPorJugar.setTextColor(Color.parseColor("#005b7d"));
                break;

            case 2: textViewFinalizado.setBackgroundColor(Color.parseColor("#323232"));
                textViewFinalizado.setTextColor(Color.parseColor("#FFFFFF"));
                textViewEnVivo.setBackgroundColor(Color.parseColor("#FFFFFF"));
                textViewPorJugar.setBackgroundColor(Color.parseColor("#FFFFFF"));
                textViewEnVivo.setTextColor(Color.parseColor("#005b7d"));
                textViewPorJugar.setTextColor(Color.parseColor("#005b7d"));
                break;
        }
    }

    private void putViewPagerAtPosition(int position){
        viewPagerMatches.setCurrentItem(position);
    }

    private class MyPagerAdapter extends PagerAdapter {

        private ArrayList<Match> arrayListMatches;

        public MyPagerAdapter(ArrayList<Match> arrayListMatches){
            this.arrayListMatches = arrayListMatches;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            layout.setLayoutParams(layoutParams);

            ListView lv = new ListView(getActivity());

            lv.setAdapter(new MatchListAdapter(arrayListMatches, getActivity(), lv, filterBet));

            lv.setDivider(new ColorDrawable(Color.TRANSPARENT));

            layout.addView(lv);
            container.addView(layout);

            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
