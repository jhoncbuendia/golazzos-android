package games.buendia.jhon.golazzos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import games.buendia.jhon.golazzos.R;
import games.buendia.jhon.golazzos.adapters.CustomSpinnerAdapter;
import games.buendia.jhon.golazzos.adapters.WeeklyWinnersAdapter;
import games.buendia.jhon.golazzos.model.WeeklyWinner;
import games.buendia.jhon.golazzos.queryService.BuilderJsonList;
import games.buendia.jhon.golazzos.queryService.HttpRequest;
import games.buendia.jhon.golazzos.queryService.RequestInterface;
import games.buendia.jhon.golazzos.utils.DialogHelper;
import games.buendia.jhon.golazzos.utils.ServicesCall;

/**
 * Created by User on 29/03/2016.
 */
public class WeeklyAwardsActivity extends FragmentActivity implements RequestInterface, AdapterView.OnItemSelectedListener {

    private String url;
    private BuilderJsonList builderJsonList;
    private ArrayList<WeeklyWinner> weeklyWinnersArrayList;
    private Spinner spinnerDates;
    private ListView listViewWinners;
    private ImageButton imageButtonBack;
    private LinearLayout nivelLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_winners);
        url = String.format(getString(R.string.format_url), getString(R.string.url_base), getString(R.string.weekly_awards_endpoint));
        DialogHelper.showLoaderDialog(WeeklyAwardsActivity.this);

        spinnerDates = (Spinner) findViewById(R.id.spinnerCalendario);
        listViewWinners = (ListView) findViewById(R.id.listViewWeeklyWinners);
        imageButtonBack = (ImageButton) findViewById(R.id.imageButtonBackArrow);
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        nivelLinearLayout = (LinearLayout) findViewById(R.id.linearLayoutNivel);
        nivelLinearLayout.setClickable(true);
        nivelLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WeeklyAwardsActivity.this, RankingActivity.class));
                finish();
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HttpRequest h = new HttpRequest(WeeklyAwardsActivity.this, ServicesCall.WEEKLY_AWARDS);
                h.sendAuthenticatedPostRequest(getApplicationContext(), url);
            }
        });
    }

    @Override
    public void onSuccessCallBack(JSONObject response, ServicesCall serviceCall) {
        builderJsonList = new BuilderJsonList(response);

        switch (serviceCall) {
            case WEEKLY_AWARDS: DialogHelper.hideLoaderDialog();
                                try {
                                    weeklyWinnersArrayList = builderJsonList.getWeeklyWinners();
                                    listViewWinners.setAdapter(new WeeklyWinnersAdapter(weeklyWinnersArrayList.get(0).getWinnersArrayList(),this, listViewWinners));
                                    String[] calendarDatesArray = new String[weeklyWinnersArrayList.size()+1];

                                    for (int i = 0; i < weeklyWinnersArrayList.size(); i++) {
                                        calendarDatesArray[i] = weeklyWinnersArrayList.get(i).getLabelDate();
                                    }

                                    spinnerDates.setAdapter(new CustomSpinnerAdapter(this, calendarDatesArray));
                                    spinnerDates.setOnItemSelectedListener(this);
                                }
                                catch (JSONException e){
                                    Log.i("JE", "here");
                                }
                                break;
        }
    }

    @Override
    public void onErrorCallBack(JSONObject response) {
        Log.i("Response-error", response.toString());
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        listViewWinners.setAdapter(new WeeklyWinnersAdapter(weeklyWinnersArrayList.get(position).getWinnersArrayList(), this, listViewWinners));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MatchListActivity.class));
        finish();
    }
}