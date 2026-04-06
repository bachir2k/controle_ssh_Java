package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientTest {
    
        public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);

            System.out.println("Connecté au serveur localhost sur le port 5000");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true
            );

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("Commande : ");
                String cmd = scanner.nextLine();

                out.println(cmd);

                String reponse;
                while ((reponse = in.readLine()) != null) {
                    System.out.println(reponse);
                    if (!in.ready()) break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
