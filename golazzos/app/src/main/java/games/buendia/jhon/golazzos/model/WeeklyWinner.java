package games.buendia.jhon.golazzos.model;

import java.util.ArrayList;

/**
 * Created by User on 29/03/2016.
 */
public class WeeklyWinner {

    private String labelDate;
    private ArrayList<Winner> winnersArrayList;

    public WeeklyWinner(){

    }

    public String getLabelDate() {
        return labelDate;
    }

    public void setLabelDate(String labelDate) {
        this.labelDate = labelDate;
    }

    public ArrayList<Winner> getWinnersArrayList() {
        return winnersArrayList;
    }

    public void setWinnersArrayList(ArrayList<Winner> winnersArrayList) {
        this.winnersArrayList = winnersArrayList;
    }
}
