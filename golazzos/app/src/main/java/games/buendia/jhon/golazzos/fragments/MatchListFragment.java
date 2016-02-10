package games.buendia.jhon.golazzos.fragments;

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

import java.util.ArrayList;
import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.activities.MatchListActivity;
import games.buendia.jhon.golazzos.adapters.CustomSpinnerAdapter;
import games.buendia.jhon.golazzos.adapters.MatchListAdapter;
import games.buendia.jhon.golazzos.model.Match;
import games.buendia.jhon.golazzos.model.Team;
import games.buendia.jhon.golazzos.model.Tournament;

/**
 * Created by User on 07/02/2016.
 */
public class MatchListFragment extends Fragment {

    private boolean spinnerLigasPressed = false;
    private boolean spinnerEquiposPressed = false;
    private ViewPager viewPagerMatches;
    private TextView textViewPorJugar, textViewEnVivo, textViewFinalizado;

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

        viewPagerMatches = (ViewPager) view.findViewById(R.id.viewPagerMatches);
        viewPagerMatches.setAdapter(new MyPagerAdapter(matches));
        viewPagerMatches.setCurrentItem(0);

        textViewPorJugar = (TextView) view.findViewById(R.id.textViewPorJugar);
        textViewEnVivo = (TextView) view.findViewById(R.id.textViewEnVivo);
        textViewFinalizado = (TextView) view.findViewById(R.id.textViewFinalizado);

        textViewFinalizado.setClickable(true);
        textViewEnVivo.setClickable(true);
        textViewPorJugar.setClickable(true);

        textViewPorJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putViewPagerAtPosition(0);
            }
        });

        textViewEnVivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putViewPagerAtPosition(1);
            }
        });

        textViewFinalizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putViewPagerAtPosition(2);
            }
        });

        int indexSelected = 0;
        String[] tournamentsStringArray = new String[tournaments.size()];
        int idTournament = arguments.getInt("tournament_id");
        boolean ifIsSelected = idTournament != 0, findIt = false;

        for (int i = 0; i < tournaments.size(); i++) {
            tournamentsStringArray[i] = tournaments.get(i).getNameTornament();
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
            tournamentsStringArray[indexSelected] = tournaments.get(0).getNameTornament();
            tournamentsStringArray[0] = tournaments.get(indexSelected).getNameTornament();
        }

        String[] teamsStringArray = new String[teams.size()];
        for (int i = 0; i < teams.size(); i++)
            teamsStringArray[i] = teams.get(i).getTeamName();


        final Spinner spinnerLigas = (Spinner) view.findViewById(R.id.spinnerLigas);
        final Spinner spinnerEquipos = (Spinner) view.findViewById(R.id.spinnerEquipos);

        spinnerLigas.setAdapter(new CustomSpinnerAdapter(getActivity(), tournamentsStringArray));
        spinnerEquipos.setAdapter(new CustomSpinnerAdapter(getActivity(), teamsStringArray));

        spinnerEquipos.setFocusable(false);
        spinnerLigas.setFocusable(false);

        view.findViewById(R.id.imageButtonHamburguerMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MatchListActivity) getActivity()).openDrawerMenu(view);
            }
        });

        spinnerLigas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerLigasPressed) {
                    Intent intent = new Intent(getActivity(), MatchListActivity.class);
                    intent.putExtra("tournament_id", tournaments.get(i).getIdTournament());
                    startActivity(intent);
                    getActivity().finish();
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
                   Intent intent = new Intent(getActivity(), MatchListActivity.class);
                   intent.putExtra("team_name", teams.get(i).getTeamName());
                   intent.putExtra("tournament_id", arguments.getInt("tournament_id"));
                   startActivity(intent);
                   getActivity().finish();
               }
               else spinnerEquiposPressed = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return view;
    }

    public void checkCurrentItem(){
        switch (viewPagerMatches.getCurrentItem()){
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
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            checkCurrentItem();
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

            lv.setAdapter(new MatchListAdapter(arrayListMatches, getActivity(), lv));

            lv.setDivider(new ColorDrawable(Color.TRANSPARENT));

            layout.addView(lv);
            checkCurrentItem();
            container.addView(layout);

            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
