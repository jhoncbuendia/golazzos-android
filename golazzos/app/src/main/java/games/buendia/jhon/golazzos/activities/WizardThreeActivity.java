package games.buendia.jhon.golazzos.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import games.buendia.jhon.golazzos.R;

public class WizardThreeActivity extends AppCompatActivity {

    private Context context;
    private Button wizard_four;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard_three);

        context = this;



        wizard_four = (Button)findViewById(R.id.wizard_four);
        wizard_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Log.i("status", "Runing SingUp Intent");
                Intent intent = new Intent(context, WizardFourActivity.class);
                startActivity(intent);

            }
        });

    }

}
