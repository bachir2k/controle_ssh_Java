package serveur;

import java.io.*;
import java.net.Socket;

public class GestionnaireClient implements Runnable {

    private Socket socket;

    public GestionnaireClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            System.out.println("Client connecté : " + socket.getInetAddress());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), "Cp850")
            );

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true
            );

            String commande;

            while ((commande = in.readLine()) != null) {
                System.out.println("Commande reçue : " + commande);

                String resultat = ExecuteurCommande.executer(commande);

                out.println(resultat);
            }

        } catch (Exception e) {
            System.out.println("Erreur client : " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}