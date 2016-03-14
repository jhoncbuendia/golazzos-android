package games.buendia.jhon.golazzos.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.activities.StadiumActivity;
import games.buendia.jhon.golazzos.activities.WriteSomethingActivity;
import games.buendia.jhon.golazzos.queryService.HttpRequest;
import games.buendia.jhon.golazzos.queryService.RequestInterface;
import games.buendia.jhon.golazzos.utils.DialogHelper;
import games.buendia.jhon.golazzos.utils.JSONBuilder;
import games.buendia.jhon.golazzos.utils.PreferencesHelper;
import games.buendia.jhon.golazzos.utils.ServicesCall;

/**
 * Created by User on 28/02/2016.
 */
public class WriteSomethingFragment extends Fragment implements RequestInterface{

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

        String urlImage = PreferencesHelper.getUrlPhoto();

        if (!urlImage.contains("http")){
            urlImage = "http:"+urlImage;
        }

        ImageView imageViewIcon = (ImageView) view.findViewById(R.id.imageViewUserIcon);

        Picasso.with(getActivity())
                .load(urlImage)
                .into(imageViewIcon);

        final EditText editTextPost = (EditText) view.findViewById(R.id.editTextQueTienesEnMente);

        CardView cardViewPublicar = (CardView) view.findViewById(R.id.cardViewPublicar);
        cardViewPublicar.setClickable(true);
        cardViewPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editTextPost.getText().toString().isEmpty()){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONBuilder builderJson = new JSONBuilder();
                            DialogHelper.showLoaderDialog(getActivity());
                            HttpRequest h = new HttpRequest(WriteSomethingFragment.this);
                            String url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.posts_endpoint));
                            h.startPostRequestAuthenticated(getActivity(), url, builderJson.getPostJson(editTextPost.getText().toString()), 0);
                        }
                    });
                }
                else {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.error_mensaje_vacio), Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    @Override
    public void onSuccessCallBack(JSONObject response, ServicesCall serviceCall) {
        DialogHelper.hideLoaderDialog();
        showAlertDialog(getActivity().getString(R.string.mensaje_exitoso));
    }

    @Override
    public void onErrorCallBack(JSONObject response) {
        DialogHelper.hideLoaderDialog();
        Toast.makeText(getActivity(), getActivity().getString(R.string.error_transaccional), Toast.LENGTH_SHORT).show();
    }

    private void showAlertDialog(String mensaje){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(mensaje);

        alertDialogBuilder.setPositiveButton(getString(R.string.aceptar_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                startActivity(new Intent(getActivity(), StadiumActivity.class));
                getActivity().finish();
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}