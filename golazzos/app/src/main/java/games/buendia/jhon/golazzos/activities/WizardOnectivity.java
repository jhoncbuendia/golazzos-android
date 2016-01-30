package games.buendia.jhon.golazzos.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import games.buendia.jhon.golazzos.R;

public class WizardOnectivity extends AppCompatActivity {

    private Button wizard_two;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_wizard_one);

        wizard_two = (Button)findViewById(R.id.wizard_two);
        wizard_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Log.i("status", "Runing SingUp Intent");
                Intent intent = new Intent(context, WizardTwoActivity.class);
                startActivity(intent);

            }
        });

        /*WizardOneFragment l = new WizardOneFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.wizardone_cont, l).commit();*/


    }

}
