package games.buendia.jhon.golazzos.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.content.pm.Signature;
import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.utils.PreferencesHelper;

public class InitialActivity extends AppCompatActivity {

    private Button create;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!PreferencesHelper.isUserLogged()) {

            setContentView(R.layout.activity_initial);

            try {
                PackageInfo info = getPackageManager().
                        getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);

                for (Signature signature : info.signatures) {

                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());


                    Log.d("====Hash Key===", Base64.encodeToString(md.digest(),
                            Base64.DEFAULT));

                }

            } catch (PackageManager.NameNotFoundException e) {

            } catch (NoSuchAlgorithmException ex) {

            }

            create = (Button) findViewById(R.id.create);
            login = (Button) findViewById(R.id.login);

            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(intent);
                    finish();

                }
            });
        }
        else {
            Intent intent = new Intent(this, MatchListActivity.class);
            startActivity(intent);
            finish();
        }

    }

}
