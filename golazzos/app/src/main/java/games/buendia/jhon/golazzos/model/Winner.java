package games.buendia.jhon.golazzos.model;

/**
 * Created by User on 29/03/2016.
 */
public class Winner {

    private int rank;
    private int pointsSize;
    private String winnerName;
    private String winnerSoulTeam;
    private String urlProfilePicture;
    private String urlSoulTeamPicture;

    public Winner(int rank, int pointsSize, String winnerName, String winnerSoulTeam, String urlProfilePicture, String urlSoulTeamPicture) {
        this.rank = rank;
        this.winnerSoulTeam = winnerSoulTeam;
        this.pointsSize = pointsSize;
        this.winnerName = winnerName;
        this.urlProfilePicture = urlProfilePicture;
        this.urlSoulTeamPicture = urlSoulTeamPicture;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public String getWinnerSoulTeam() {
        return winnerSoulTeam;
    }

    public void setWinnerSoulTeam(String winnerSoulTeam) {
        this.winnerSoulTeam = winnerSoulTeam;
    }

    public String getUrlSoulTeamPicture() {
        return urlSoulTeamPicture;
    }

    public void setUrlSoulTeamPicture(String urlSoulTeamPicture) {
        this.urlSoulTeamPicture = urlSoulTeamPicture;
    }

    public String getUrlProfilePicture() {
        return urlProfilePicture;
    }

    public void setUrlProfilePicture(String urlProfilePicture) {
        this.urlProfilePicture = urlProfilePicture;
    }

    public int getPointsSize() {
        return pointsSize;
    }

    public void setPointsSize(int pointsSize) {
        this.pointsSize = pointsSize;
    }
}