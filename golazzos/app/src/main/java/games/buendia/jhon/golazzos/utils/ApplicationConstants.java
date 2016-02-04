package games.buendia.jhon.golazzos.utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by User on 29/01/2016.
 */
public class ApplicationConstants {

    // Estas constantes son únicamente para pruebas.

    public static final ArrayList<String> ligas = new ArrayList<String>
            (Arrays.asList("Liga", "Liga BBVA","Serie A","Bundesliga","Liga Aguila","Premier League"));

    public static final ArrayList<String> equiposLigaBBVA = new ArrayList<String>
            (Arrays.asList("Equipo", "FC Barcelona","Real Madrid","Atletico","Valencia"));

    public static final ArrayList<String> equiposSerieA = new ArrayList<String>
            (Arrays.asList("Equipo", "Roma","Inter","Juventus","Milan"));

    public static final ArrayList<String> equiposBundesliga = new ArrayList<String>
            (Arrays.asList("Equipo", "Dortmund","Bayern","Leverkusen","Frankfurt"));

    public static final ArrayList<String> equiposLigaAguila = new ArrayList<String>
            (Arrays.asList("Equipo", "Nacional","Junior","Santa Fe","Millonarios"));

    public static final ArrayList<String> equiposPremier = new ArrayList<String>
            (Arrays.asList("Equipo", "Man City","Arsenal","Chelsea","Man Utd"));

    public static final ArrayList<String> urlsIcons = new ArrayList<String>
            (Arrays.asList("http://www.idfootballdesk.com/media/catalog/product/cache/1/small_image/300x400/9df78eab33525d08d6e5fb8d27136e95/b/a/barcelona-home-jersey-2015-16.png",
                           "http://www.uksoccershop.com/images/as-roma-2015-2016-nike-core-polo-shirt-red.jpg",
                           "http://www.uksoccershop.com/images/borussia-dortmund-home-shirt-2012-13.jpg",
                           "http://www.footballkitnews.com/wp-content/uploads/2015/01/Atletico-Nacional-Home-Shirt-2015.jpg",
                           "http://www.footballkitnews.com/wp-content/uploads/2013/05/New-Man-City-Nike-Kit-13-14.jpg"));

    // Constantes para manejo de preferencias (aqui se va a guardar el objeto usuario)

    public static final String keyPreferences = "myPreferences";
    public static final String idKey = "idKey";
    public static final String nameKey = "nameKey";
    public static final String emailKey = "emailKey";
    public static final String avatarKey = "avatarKey";
    public static final String soulTeamKey = "soulTeamKey";

}