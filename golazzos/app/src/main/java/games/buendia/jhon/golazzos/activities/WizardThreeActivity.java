package games.buendia.jhon.golazzos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import games.buendia.jhon.golazzos.R;

public class WizardThreeActivity extends AppCompatActivity {

    private Button wizardFour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard_three);
        wizardFour = (Button)findViewById(R.id.buttonSiguiente);
        wizardFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(WizardThreeActivity.this, WizardFourActivity.class);
                startActivity(intent);
            }
        });

    }

}