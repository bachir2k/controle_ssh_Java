package serveur;

import java.io.*;
import java.net.Socket;
import commun.Config;

public class GestionnaireClient implements Runnable {

    private Socket socket;
    private int clientId;

    public GestionnaireClient(Socket socket, int id) {
        this.socket = socket;
        this.clientId = id;
    }

    @Override
    public void run() {
        try {
            System.out.println("[" + clientId + "] Client connecté : " + socket.getInetAddress());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), "Cp850")
            );

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true
            );

            String commande;

            while ((commande = in.readLine()) != null) {
                System.out.println("Commande reçue : " + commande);

                if (Config.CMD_EXIT.equals(commande.trim())) {
                    System.out.println("Déconnexion demandée par le client.");
                    break;
                }

                String resultat = ExecuteurCommande.executer(commande);

                out.println(resultat);
                
            }

        } catch (Exception e) {
            System.out.println("Erreur client (" + socket.getInetAddress() + ") : " + e.getMessage());
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
                System.out.println("[" + clientId + "] Session terminée pour : " + socket.getInetAddress());
            } catch (IOException e) {
                System.err.println("Erreur lors de la fermeture du socket : " + e.getMessage());
            }
        }
    }
}