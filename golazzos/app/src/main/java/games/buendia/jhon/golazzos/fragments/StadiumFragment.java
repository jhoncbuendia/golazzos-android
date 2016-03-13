package games.buendia.jhon.golazzos.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.activities.MatchListActivity;
import games.buendia.jhon.golazzos.activities.StadiumActivity;
import games.buendia.jhon.golazzos.activities.StoryDetailActivity;
import games.buendia.jhon.golazzos.activities.UpdateFavoriteTeamsActivity;
import games.buendia.jhon.golazzos.activities.UpdateSoulTeamActivity;
import games.buendia.jhon.golazzos.activities.WriteSomethingActivity;
import games.buendia.jhon.golazzos.adapters.TimeLineAdapter;
import games.buendia.jhon.golazzos.model.Story;
import games.buendia.jhon.golazzos.utils.ApplicationConstants;
import games.buendia.jhon.golazzos.utils.PreferencesHelper;

/**
 * Created by User on 27/02/2016.
 */
public class StadiumFragment extends Fragment {

    public StadiumFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_stadium, container, false);

        view.findViewById(R.id.imageButtonHamburguerMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((StadiumActivity) getActivity()).openDrawerMenu(view);
            }
        });

        //TODO - remove this, when the services is ready.
        final ArrayList<Story> storyArrayList = new ArrayList<Story>();
        ArrayList<Story> storyArrayListTest = ApplicationConstants.storiesArrayList;

        for (Story story: storyArrayListTest){
            story.setUrlImage(PreferencesHelper.getUrlPhoto());
            story.setTimeAgo(getActivity().getString(R.string.hace_dos_minutos));
            storyArrayList.add(story);
        }

        LinearLayout layoutFanaticada = (LinearLayout) view.findViewById(R.id.layoutFanaticada);
        LinearLayout layoutFavoritos = (LinearLayout) view.findViewById(R.id.layoutFavoritos);
        LinearLayout layoutPartidos = (LinearLayout) view.findViewById(R.id.layoutPartidos);
        LinearLayout layoutRanking = (LinearLayout) view.findViewById(R.id.layoutRanking);
        LinearLayout layoutAmigos = (LinearLayout) view.findViewById(R.id.layoutAmigos);

        layoutPartidos.setClickable(true);
        layoutPartidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MatchListActivity.class));
                getActivity().finish();
            }
        });

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

        TextView textViewQueTienesEnMente = (TextView) view.findViewById(R.id.textViewQueTienesEnMente);
        textViewQueTienesEnMente.setClickable(true);
        textViewQueTienesEnMente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), WriteSomethingActivity.class));
                getActivity().finish();
            }
        });

        ListView listViewTimeLine = (ListView) view.findViewById(R.id.listViewTimeLine);
        listViewTimeLine.setAdapter(new TimeLineAdapter(storyArrayList, getActivity(), listViewTimeLine));

        listViewTimeLine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), StoryDetailActivity.class);
                intent.putExtra("story", storyArrayList.get(position));
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}
