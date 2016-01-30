package games.buendia.jhon.golazzos.model;

/**
 * Created by User on 30/01/2016.
 */
public class Team {

    private String teamName;
    private String urlTeam;

    public Team(String teamName){
        this.setTeamName(teamName);
    }

    public Team(String teamName, String urlTeam){
        this.setTeamName(teamName);
        this.setUrlTeam(urlTeam);
    }


    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getUrlTeam() {
        return urlTeam;
    }

    public void setUrlTeam(String urlTeam) {
        this.urlTeam = urlTeam;
    }
}