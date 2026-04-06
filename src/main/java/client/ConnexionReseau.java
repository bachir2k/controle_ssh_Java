package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import commun.Config;


public class ConnexionReseau {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean connecte = false;

    /**
     * Se connecte au serveur.
     */
    public void connecter(String ip, int port) throws Exception {
        socket = new Socket(ip, port);
        socket.setSoTimeout(Config.SOCKET_TIMEOUT);

        in = new BufferedReader(
            new InputStreamReader(socket.getInputStream(), "UTF-8")
        );
        out = new PrintWriter(
            new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true
        );

        connecte = true;
        System.out.println("[Client] Connecté à " + ip + ":" + port);
    }

    /**
     * Envoie une commande au serveur.
     */
    public void envoyerCommande(String commande) {
        if (!connecte) throw new IllegalStateException("Non connecté.");
        out.println(commande);
    }

    /**
     * Lit le résultat jusqu'au marqueur ##END##.
     */
    public String lireResultat() throws Exception {
        if (!connecte) throw new IllegalStateException("Non connecté.");

        StringBuilder sb = new StringBuilder();
        String ligne;

        while ((ligne = in.readLine()) != null) {
            if (Config.END_OF_RESULT.equals(ligne)) break;
            sb.append(ligne).append("\n");
        }

        return sb.toString();
    }

    /**
     * Ferme la connexion proprement.
     */
    public void deconnecter() {
        connecte = false;
        try {
            if (out != null) out.println(Config.CMD_EXIT);
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (Exception ignored) {}
        System.out.println("[Client] Déconnecté.");
    }

    public boolean isConnecte() {
        return connecte && socket != null && !socket.isClosed();
    }
}
