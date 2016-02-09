package games.buendia.jhon.golazzos.queryService;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import games.buendia.jhon.golazzos.model.Match;
import games.buendia.jhon.golazzos.model.Team;
import games.buendia.jhon.golazzos.model.Tournament;

/**
 * Created by User on 07/02/2016.
 */
public class BuilderJsonList {

    private JSONObject jsonObject;

    public BuilderJsonList(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public boolean isWizardCompleted() throws JSONException {
        return jsonObject.getJSONObject("soul_team") != null;
    }

    public ArrayList<Team> getTeams() throws JSONException {
        JSONArray teams = jsonObject.getJSONArray("response");
        ArrayList<Team> teamArrayList = new ArrayList<Team>();

        for (int i = 0; i < teams.length(); i++){
            JSONObject teamObject = teams.getJSONObject(i);
            teamArrayList.add(new Team(teamObject.getInt("id"),
                    teamObject.getString("name")));
        }
        return teamArrayList;

    }

    public ArrayList<Team> getTeamsWithImages() throws JSONException {
        JSONArray teams = jsonObject.getJSONArray("response");
        ArrayList<Team> teamArrayList = new ArrayList<Team>();

        for (int i = 0; i < teams.length(); i++){
            JSONObject teamObject = teams.getJSONObject(i);
            teamArrayList.add(new Team(teamObject.getInt("id"),
                    teamObject.getString("name"), teamObject.getString("image_path")));
        }
        return teamArrayList;

    }

    public ArrayList<Tournament> getTournaments() throws JSONException {
        JSONArray tournaments = jsonObject.getJSONArray("response");
        ArrayList<Tournament> tournamentArrayList = new ArrayList<Tournament>();

        for (int i = 0; i < tournaments.length(); i++){
            JSONObject tournamentObject = tournaments.getJSONObject(i);
            tournamentArrayList.add(new Tournament(tournamentObject.getInt("id"),
                                                   tournamentObject.getString("name")));
        }
        return tournamentArrayList;
    }

    public ArrayList<Match> getMatches() throws JSONException {

        JSONArray matches = jsonObject.getJSONArray("response");
        ArrayList<Match> matchArrayList = new ArrayList<Match>();

        for (int i = 0; i < matches.length(); i++){

            JSONObject matchObject = matches.getJSONObject(i);
            JSONObject localTeamObject = matchObject.getJSONObject("local_team");
            JSONObject awayTeamObject = matchObject.getJSONObject("visitant_team");
            JSONObject tourtamentObject = matchObject.getJSONObject("tournament");

            Tournament tournament = new Tournament(tourtamentObject.getInt("id"), tourtamentObject.getString("name"));

            Team localTeam = new Team(localTeamObject.getInt("id"),
                                      localTeamObject.getString("name"),
                                      localTeamObject.getString("image_path"),
                                      tournament);

            Team awayTeam = new Team(awayTeamObject.getInt("id"),
                                     awayTeamObject.getString("name"),
                                     awayTeamObject.getString("image_path"),
                                     tournament);

            Match match = new Match(matchObject.getInt("id"), matchObject.getString("start_time_utc"),
                                    null, localTeam, awayTeam);

            matchArrayList.add(match);
        }

        return matchArrayList;
    }
}
