package games.buendia.jhon.golazzos.model;

import java.io.Serializable;

/**
 * Created by User on 07/02/2016.
 */
public class Tournament implements Serializable{
    private int idTournament;
    private String nameTornament;

    public Tournament(int idTournament, String nameTornament) {
        this.idTournament = idTournament;
        this.nameTornament = nameTornament;
    }

    public int getIdTournament() {
        return idTournament;
    }

    public void setIdTournament(int idTournament) {
        this.idTournament = idTournament;
    }

    public String getNameTornament() {
        return nameTornament;
    }

    public void setNameTornament(String nameTornament) {
        this.nameTornament = nameTornament;
    }
}
