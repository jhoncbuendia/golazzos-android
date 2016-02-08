package games.buendia.jhon.golazzos.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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

        ViewPager viewPagerMatches = (ViewPager) view.findViewById(R.id.viewPagerMatches);
        viewPagerMatches.setAdapter(new MyPagerAdapter(matches));
        viewPagerMatches.setCurrentItem(0);

        String[] tournamentsStringArray = new String[tournaments.size()];
        for (int i = 0; i < tournaments.size(); i++)
            tournamentsStringArray[i] = tournaments.get(i).getNameTornament();

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

            container.addView(layout);

            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout)object);
        }
    }
}
