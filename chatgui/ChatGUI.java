package chatgui;

import javax.swing.*;

public class ChatGUI extends JFrame {

    public ChatGUI() {
        // Configuração da janela
        setTitle("Chat");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Adiciona o ChatPanel à janela
        add(new ChatPanel());

        // Exibe a janela
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatGUI());
    }
}