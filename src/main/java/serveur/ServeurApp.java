package serveur;

import java.net.ServerSocket;
import java.net.Socket;

public class ServeurApp {

    public static void main(String[] args) {
        int port = 5000;

        try {
            ServerSocket serveur = new ServerSocket(port);
            System.out.println("\n Serveur démarré sur le port " + port);

            int clientCount = 0;
            while (true) {
                Socket client = serveur.accept();
                clientCount++;
                System.out.println("Nouvelle connexion ! Client #" + clientCount);

                GestionnaireClient gestionnaire = new GestionnaireClient(client, clientCount);
                Thread thread = new Thread(gestionnaire);
                thread.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}