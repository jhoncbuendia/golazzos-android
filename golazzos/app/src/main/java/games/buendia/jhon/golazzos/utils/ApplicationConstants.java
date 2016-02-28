package games.buendia.jhon.golazzos.utils;

import java.util.ArrayList;
import java.util.Arrays;

import games.buendia.jhon.golazzos.model.Story;

/**
 * Created by User on 29/01/2016.
 */
public class ApplicationConstants {

    public static final String[] pronosticos = {"Gana Visitante", "Gana Local", "Empate"};

    // Constantes para manejo de preferencias (aqui se va a guardar el objeto usuario)

    public static final String keyPreferences = "myPreferences";
    public static final String tokenKey = "tokenKey";
    public static final String jsonKeyJwt = "jwt";
    public static final String userLoggedKey = "userLoggedKey";

    public static final String idUserKey = "idUserKey";
    public static final String nameUserKey = "nameUserKey";
    public static final String pointsUserKey = "pointsUserKey";
    public static final String emailUserKey = "emailUserKey";
    public static final String urlImageKey = "urlImageKey";
    public static final String paidSubscriptionKey = "paidSubscriptionKey";
    public static final String soutTeamIdKey = "soutTeamIdKey";
    public static final String soutTeamNameKey = "soutTeamNameKey";
    public static final String soulTeamImageUrlKey = "soulTeamImageUrlKey";

    public static String[] opcionesAlerta = {" - Cambiar equipo del alma", " - Cambiar equipos favoritos"};
    public static String[] pointsToBet = {"50", "100"};

    public static final String formatDate = "yyyy/MM/dd HH:mm:ss";
    public static final String formatDateMatch = "MMM dd, hh:mm a";

    public static ArrayList<Story> storiesArrayList = new ArrayList<Story>(Arrays.asList(new Story("Carlos Díaz perdio jugando al Millonarios.."),new Story("Marlon Mantilla esta jugando 50 puntos a que habran 6 corners en Manchester City"), new Story("¡Carlos Granados ha ganado 100 puntos!"), new Story("Dani Merchan esta jugando 50 puntos a que habran 6 corners en Manchester City"), new Story("Andres Saldarriaga ha subido al Nivel 7")));

}