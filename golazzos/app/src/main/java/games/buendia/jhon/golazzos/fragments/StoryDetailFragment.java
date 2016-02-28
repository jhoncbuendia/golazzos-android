package games.buendia.jhon.golazzos.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.activities.StadiumActivity;
import games.buendia.jhon.golazzos.activities.StoryDetailActivity;
import games.buendia.jhon.golazzos.model.Story;

/**
 * Created by User on 28/02/2016.
 */
public class StoryDetailFragment extends Fragment{

    public StoryDetailFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragmen_detail_history, container, false);

        Bundle arguments = getArguments();
        Story story = (Story) arguments.getSerializable("story");

        view.findViewById(R.id.imageButtonHamburguerMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((StoryDetailActivity) getActivity()).openDrawerMenu(view);
            }
        });

        TextView textViewCancelar = (TextView) view.findViewById(R.id.textViewCancelar);
        textViewCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), StadiumActivity.class));
                getActivity().finish();
            }
        });

        view.findViewById(R.id.imageViewFlecha).setVisibility(View.GONE);
        TextView textViewDescription = (TextView) view.findViewById(R.id.textViewDescripcion);
        textViewDescription.setEllipsize(null);
        textViewDescription.setText(story.getDescription());

        String urlImage = story.getUrlImage();

        if (!urlImage.contains("http")){
            urlImage = "http:"+urlImage;
        }

        ImageView imageViewIcon = (ImageView) view.findViewById(R.id.imageViewIcono);

        Picasso.with(getActivity())
                .load(urlImage)
                .into(imageViewIcon);


        return view;
    }
}
