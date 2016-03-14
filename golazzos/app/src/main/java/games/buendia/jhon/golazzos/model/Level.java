package games.buendia.jhon.golazzos.model;

import java.io.Serializable;

/**
 * Created by User on 13/03/2016.
 */
public class Level implements Serializable{

    private String name;
    private int order;
    private int points;
    private int hitsCount;
    private String urlPhoto;

    public Level(String name, int order, int hitsCount, int points, String urlPhoto) {
        this.name = name;
        this.order = order;
        this.hitsCount = hitsCount;
        this.points = points;
        this.urlPhoto = urlPhoto;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getHitsCount() {
        return hitsCount;
    }

    public void setHitsCount(int hitsCount) {
        this.hitsCount = hitsCount;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
