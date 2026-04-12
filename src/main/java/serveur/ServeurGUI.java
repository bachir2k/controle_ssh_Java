package serveur;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import commun.Config;

public class ServeurGUI extends JFrame {

    private JTextArea logArea;
    private JButton toggleButton;
    private JLabel statusLabel;

    private ServerSocket serverSocket;
    private boolean isRunning = false;
    private List<Thread> clientThreads = new ArrayList<>();

    public ServeurGUI() {
        setTitle("Contrôle SSH - Serveur");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // -- Components --
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(new Color(10, 25, 47));
        logArea.setForeground(new Color(200, 225, 255));
        logArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        JScrollPane logScroll = new JScrollPane(logArea);

        toggleButton = new JButton("Démarrer le Serveur");
        statusLabel = new JLabel("Statut : Arrêté");
        statusLabel.setForeground(new Color(255, 100, 100));

        // -- Layout --
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(20, 40, 70));
        topPanel.add(toggleButton);
        topPanel.add(statusLabel);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(logScroll, BorderLayout.CENTER);

        // -- Events --
        toggleButton.addActionListener(e -> toggleServer());
    }

    private void toggleServer() {
        if (!isRunning) {
            startServer();
        } else {
            stopServer();
        }
    }

    private void startServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(Config.PORT);
                isRunning = true;
                
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Statut : En cours (Port " + Config.PORT + ")");
                    statusLabel.setForeground(new Color(0, 150, 0));
                    toggleButton.setText("Arrêter le Serveur");
                    appendLog("Serveur démarré sur le port " + Config.PORT + "\n");
                });

                int clientCount = 0;
                while (isRunning) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        clientCount++;
                        final int id = clientCount;

                        // Création d'un gestionnaire qui peut notifier cette UI
                        GestionnaireClient gestionnaire = new GestionnaireClient(clientSocket, id) {
                            @Override
                            protected void log(String message) {
                                super.log(message); // Garde le log console
                                appendLog("[" + id + "] " + message + "\n");
                            }

                            @Override
                            public void run() {
                                super.run();
                            }
                        };
                        
                        Thread t = new Thread(gestionnaire);
                        clientThreads.add(t);
                        t.start();
                        
                        SwingUtilities.invokeLater(() -> appendLog("[Server] Nouveau client #" + id + " (" + clientSocket.getInetAddress() + ")\n"));

                    } catch (IOException e) {
                        if (isRunning) appendLog("Erreur acceptation client : " + e.getMessage() + "\n");
                    }
                }
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> appendLog("Impossible de démarrer le serveur : " + e.getMessage() + "\n"));
            }
        }).start();
    }

    private void stopServer() {
        try {
            isRunning = false;
            if (serverSocket != null) serverSocket.close();
            
            SwingUtilities.invokeLater(() -> {
                statusLabel.setText("Statut : Arrêté");
                statusLabel.setForeground(Color.RED);
                toggleButton.setText("Démarrer le Serveur");
                appendLog("Serveur arrêté.\n");
            });
        } catch (IOException e) {
            appendLog("Erreur lors de l'arrêt : " + e.getMessage() + "\n");
        }
    }

    private void appendLog(String text) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(text);
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new ServeurGUI().setVisible(true));
    }
}
