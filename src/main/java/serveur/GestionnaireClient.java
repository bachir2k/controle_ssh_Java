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
            log("Client connecté : " + socket.getInetAddress());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), "Cp850")
            );

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true
            );

            String commande;

            while ((commande = in.readLine()) != null) {
                log("Commande reçue : " + commande);

                if (Config.CMD_EXIT.equals(commande.trim())) {
                    log("Déconnexion demandée par le client.");
                    break;
                }

                String resultat = ExecuteurCommande.executer(commande);

                out.println(resultat);
                
            }

        } catch (Exception e) {
            log("Erreur client : " + e.getMessage());
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
                log("Session terminée pour : " + socket.getInetAddress());
            } catch (IOException e) {
                System.err.println("Erreur lors de la fermeture du socket : " + e.getMessage());
            }
        }
    }

    protected void log(String message) {
        System.out.println("[" + clientId + "] " + message);
    }
}