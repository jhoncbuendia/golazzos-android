package games.buendia.jhon.golazzos.model;

/**
 * Created by User on 30/01/2016.
 */
public class Team {

    private String teamName;
    private String urlTeam;
    private int idTeam;
    private String initialsTeam;
    private String completeTeamName;
    private String countryTeam;
    private Tournament tournamentTeam;

    public Team(String teamName){
        this.setTeamName(teamName);
    }

    public Team(String teamName, String urlTeam){
        this.setTeamName(teamName);
        this.setUrlTeam(urlTeam);
    }

    public Team(int idTeam, String teamName){
        this.setTeamName(teamName);
        this.setIdTeam(idTeam);
    }

    public Team(String urlTeam, int idTeam, String initialsTeam, String completeTeamName, String countryTeam, Tournament tournamentTeam) {
        this.urlTeam = urlTeam;
        this.idTeam = idTeam;
        this.initialsTeam = initialsTeam;
        this.completeTeamName = completeTeamName;
        this.countryTeam = countryTeam;
        this.tournamentTeam = tournamentTeam;

    }

    public int getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(int idTeam) {
        this.idTeam = idTeam;
    }

    public String getInitialsTeam() {
        return initialsTeam;
    }

    public void setInitialsTeam(String initialsTeam) {
        this.initialsTeam = initialsTeam;
    }

    public String getCompleteTeamName() {
        return completeTeamName;
    }

    public void setCompleteTeamName(String completeTeamName) {
        this.completeTeamName = completeTeamName;
    }

    public String getCountryTeam() {
        return countryTeam;
    }

    public void setCountryTeam(String countryTeam) {
        this.countryTeam = countryTeam;
    }


    public Tournament getTournamentTeam() {
        return tournamentTeam;
    }

    public void setTournamentTeam(Tournament tournamentTeam) {
        this.tournamentTeam = tournamentTeam;
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