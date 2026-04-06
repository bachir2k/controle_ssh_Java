package serveur;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExecuteurCommande {

    public static String executer(String commande) {
        StringBuilder resultat = new StringBuilder();

        try {
            ProcessBuilder pb = new ProcessBuilder();

            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                pb.command("cmd.exe", "/c", commande);
            } else {
                pb.command("/bin/sh", "-c", commande);
            }

            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "Cp850")
            );

            String ligne;
            while ((ligne = reader.readLine()) != null) {
                resultat.append(ligne).append("\n");
            }

            process.waitFor();

        } catch (Exception e) {
            resultat.append("Erreur : ").append(e.getMessage());
        }

        return resultat.toString();
    }
}