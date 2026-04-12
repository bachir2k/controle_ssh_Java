package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import commun.Config;

public class ClientGUI extends JFrame {

    private JTextArea logArea;
    private JTextField commandField;
    private JButton sendButton;
    private JLabel statusLabel;

    private java.util.List<String> commandHistory = new java.util.ArrayList<>();
    private int historyIndex = -1;
    
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientGUI() {
        setTitle("Contrôle SSH - Client");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // -- UI Components --
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(new Color(10, 25, 47)); // Navy Blue background
        logArea.setForeground(new Color(200, 225, 255)); // Light blue text
        logArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(logArea);

        commandField = new JTextField();
        commandField.setBackground(new Color(20, 40, 70));
        commandField.setForeground(Color.WHITE);
        commandField.setCaretColor(Color.WHITE);
        commandField.setFont(new Font("Consolas", Font.PLAIN, 14));
        commandField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 100, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        sendButton = new JButton("Envoyer");
        statusLabel = new JLabel("Statut : Déconnecté");
        statusLabel.setForeground(Color.RED);

        // -- Layout --
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(10, 25, 47));
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(10, 25, 47));
        bottomPanel.add(commandField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(20, 40, 70));
        topPanel.add(statusLabel);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // -- Listeners --
        ActionListener sendAction = e -> envoyerCommande();
        sendButton.addActionListener(sendAction);
        commandField.addActionListener(sendAction);

        commandField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    naviguerHistorique(-1);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    naviguerHistorique(1);
                }
            }
        });

        // -- Start Connection --
        connecter();
    }

    private void naviguerHistorique(int direction) {
        if (commandHistory.isEmpty()) return;

        int newIndex = historyIndex + direction;

        if (newIndex >= 0 && newIndex < commandHistory.size()) {
            historyIndex = newIndex;
            commandField.setText(commandHistory.get(historyIndex));
        } else if (newIndex >= commandHistory.size()) {
            historyIndex = commandHistory.size();
            commandField.setText("");
        }
    }

    private void connecter() {
        new Thread(() -> {
            try {
                socket = new Socket(Config.DEFAULT_IP, Config.PORT);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "Cp850"));

                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Statut : Connecté à " + Config.DEFAULT_IP);
                    statusLabel.setForeground(new Color(0, 150, 0));
                    appendLog("Connecté au serveur.\n");
                });

                String reponse;
                while ((reponse = in.readLine()) != null) {
                    final String rep = reponse;
                    SwingUtilities.invokeLater(() -> appendLog(rep + "\n"));
                }

                deconnecter("Le serveur a fermé la connexion.");

            } catch (IOException e) {
                deconnecter("Erreur de connexion : " + e.getMessage());
            }
        }).start();
    }

    private void envoyerCommande() {
        String cmd = commandField.getText().trim();
        if (cmd.isEmpty()) return;

        if (out != null) {
            appendLog("> " + cmd + "\n");
            out.println(cmd);
            
            // Ajouter à l'historique si différent de la dernière commande
            if (commandHistory.isEmpty() || !commandHistory.get(commandHistory.size() - 1).equals(cmd)) {
                commandHistory.add(cmd);
            }
            historyIndex = commandHistory.size();
            
            commandField.setText("");

            if (Config.CMD_EXIT.equalsIgnoreCase(cmd)) {
                deconnecter("Déconnexion...");
                System.exit(0);
            }
        }
    }

    private void deconnecter(String message) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText("Statut : Déconnecté");
            statusLabel.setForeground(Color.RED);
            appendLog("--- " + message + " ---\n");
        });
        
        try { if (socket != null) socket.close(); } catch (IOException e) {}
    }

    private void appendLog(String text) {
        logArea.append(text);
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        // Appliquer un look and feel système pour une meilleure intégration
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        
        SwingUtilities.invokeLater(() -> {
            new ClientGUI().setVisible(true);
        });
    }
}


