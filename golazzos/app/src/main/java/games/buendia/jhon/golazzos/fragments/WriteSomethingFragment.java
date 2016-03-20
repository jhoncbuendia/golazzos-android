package games.buendia.jhon.golazzos.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;
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

    private Uri fileUri;
    private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public  final int MEDIA_TYPE_IMAGE = 1;
    private String urlImage = "";
    private final String IMAGE_DIRECTORY_NAME = "Photos";

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
                if (!editTextPost.getText().toString().isEmpty()) {
                    DialogHelper.showLoaderDialog(getActivity());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONBuilder builderJson = new JSONBuilder();
                            JSONObject jsonToPost = builderJson.getPostJson(editTextPost.getText().toString(), encodeImageToBase64(previewCapturedImage(getOutputMediaFile(getUrlImage()))).replace("\n", "").replace("\r", ""));
                            HttpRequest h = new HttpRequest(WriteSomethingFragment.this);
                            String url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.posts_endpoint));
                            h.startPostRequestAuthenticated(getActivity(), url, jsonToPost, 0);
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.error_mensaje_vacio), Toast.LENGTH_SHORT).show();
                }
            }
        });

        RelativeLayout relativeLayoutPhoto = (RelativeLayout) view.findViewById(R.id.relativeLayoutPhoto);
        relativeLayoutPhoto.setClickable(true);
        relativeLayoutPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
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

    private void showAlertDialog(String mensaje) {

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

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        File mediaFile;

        urlImage = "IMG_" + timeStamp + ".jpg";

        setUrlImage(urlImage);

        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

            } else if (resultCode == Activity.RESULT_CANCELED) {

            } else {

            }
        }
    }

    private Bitmap previewCapturedImage(File fileUri) {

        try {

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            return bitmap;

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return null;
    }

    private File getOutputMediaFile(String mImageName){

        File mediaStorageDir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);

        return mediaFile;
    }

    private String encodeImageToBase64(Bitmap bm){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();

        return "data:image/jpeg;base64,"+Base64.encodeToString(b, Base64.NO_WRAP);

    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}