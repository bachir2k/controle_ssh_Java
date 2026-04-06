package commun;


public class Config {

    /** Port d'écoute du serveur */
    public static final int PORT = 5000;

    /** Adresse IP par défaut */
    public static final String DEFAULT_IP = "127.0.0.1";

    /** Commande pour fermer la connexion */
    public static final String CMD_EXIT = "exit";

    /** Marqueur de fin de résultat */
    public static final String END_OF_RESULT = "##END##";

    /** Mot de passe d'accès au serveur */
    public static final String SERVER_PASSWORD = "1234";

    /** Timeout socket en millisecondes */
    public static final int SOCKET_TIMEOUT = 30000;

    private Config() {}
}
