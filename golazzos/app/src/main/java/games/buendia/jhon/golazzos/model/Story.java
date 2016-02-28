package games.buendia.jhon.golazzos.model;

import java.io.Serializable;

/**
 * Created by User on 27/02/2016.
 */
public class Story implements Serializable{

    private String urlImage;
    private String timeAgo;
    private String description;

    public Story(String urlImage, String timeAgo, String description) {
        this.urlImage = urlImage;
        this.timeAgo = timeAgo;
        this.description = description;
    }

    public Story(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
