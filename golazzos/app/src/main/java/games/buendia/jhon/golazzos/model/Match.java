package games.buendia.jhon.golazzos.model;

import java.io.Serializable;

/**
 * Created by User on 07/02/2016.
 */
public class Match implements Serializable{

    private int id;
    private String timeLabelMatch;
    private String urlMatch;
    private int marcadorLocal;
    private int marcadorVisitante;
    private Team localTeam;
    private Team awayTeam;

    public Match(int id, String timeLabelMatch, String urlMatch, Team localTeam, Team awayTeam) {
        this.id = id;
        this.timeLabelMatch = timeLabelMatch;
        this.urlMatch = urlMatch;
        this.localTeam = localTeam;
        this.awayTeam = awayTeam;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimeLabelMatch() {
        return timeLabelMatch;
    }

    public void setTimeLabelMatch(String timeLabelMatch) {
        this.timeLabelMatch = timeLabelMatch;
    }

    public String getUrlMatch() {
        return urlMatch;
    }

    public void setUrlMatch(String urlMatch) {
        this.urlMatch = urlMatch;
    }

    public Team getLocalTeam() {
        return localTeam;
    }

    public void setLocalTeam(Team localTeam) {
        this.localTeam = localTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public int getMarcadorLocal() {
        return marcadorLocal;
    }

    public void setMarcadorLocal(int marcadorLocal) {
        this.marcadorLocal = marcadorLocal;
    }

    public int getMarcadorVisitante() {
        return marcadorVisitante;
    }

    public void setMarcadorVisitante(int marcadorVisitante) {
        this.marcadorVisitante = marcadorVisitante;
    }
}
