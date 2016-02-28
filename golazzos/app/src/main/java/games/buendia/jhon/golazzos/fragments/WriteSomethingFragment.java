package games.buendia.jhon.golazzos.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.activities.StadiumActivity;
import games.buendia.jhon.golazzos.activities.WriteSomethingActivity;

/**
 * Created by User on 28/02/2016.
 */
public class WriteSomethingFragment extends Fragment {

    public WriteSomethingFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_write_something, container, false);

        view.findViewById(R.id.imageButtonHamburguerMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WriteSomethingActivity) getActivity()).openDrawerMenu(view);
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

        return view;
    }
}
