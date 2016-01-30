package games.buendia.jhon.golazzos.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import games.buendia.jhon.golazzos.R;

public class WizardTwoActivity extends AppCompatActivity {

    private Context context;
    private Button wizard_three;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard_two);

        context = this;

        setContentView(R.layout.activity_wizard_two);

        wizard_three = (Button)findViewById(R.id.wizard_three);
        wizard_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Log.i("status", "Runing SingUp Intent");
                Intent intent = new Intent(context, WizardThreeActivity.class);
                startActivity(intent);

            }
        });


    }

}
