package chatgui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

public class ChatGUI extends JFrame {

    public ChatGUI() {
        // Configuração da janela
        setTitle("ChatBot PCP - BETA");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Carrega a imagem do ícone
        try {
            Image iconImage = ImageIO.read(new File("./images/chatbot_normal.png"));
            setIconImage(iconImage); 
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Adiciona o ChatPanel à janela
        add(new ChatPanel());

        // Exibe a janela
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatGUI());
    }
}