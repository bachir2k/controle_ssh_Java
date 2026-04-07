package serveur;

import java.net.ServerSocket;
import java.net.Socket;

public class ServeurApp {

    public static void main(String[] args) {
        int port = 5000;

        try {
            ServerSocket serveur = new ServerSocket(port);
            System.out.println("Serveur démarré sur le port " + port);

            while (true) {
                Socket client = serveur.accept();
                System.out.println("Nouvelle connexion !");

                GestionnaireClient gestionnaire = new GestionnaireClient(client);
                Thread thread = new Thread(gestionnaire);
                thread.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}