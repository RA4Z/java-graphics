package chatgui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ChatPanel extends JPanel {

    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JLabel statusLabel;

    public ChatPanel() {
        // Configuração do painel
        setLayout(new BorderLayout());
        setBackground(new Color(0xE8E7E7)); // Cor do chat: #e8e7e7

        // Criação dos componentes
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setBackground(new Color(0x176B87)); // Cor de fundo do output: #176b87
        chatArea.setForeground(Color.WHITE);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);

        messageField = new JTextField();
        messageField.setBackground(new Color(0x053B50)); // Cor de fundo do input: #053b50
        messageField.setForeground(Color.WHITE);
        messageField.setFont(new Font("Arial", Font.PLAIN, 14));

        sendButton = new JButton("Enviar");
        sendButton.setBackground(new Color(0x0C2D48));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("Arial", Font.PLAIN, 14));

        statusLabel = new JLabel("");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setForeground(Color.GRAY);

        // Adiciona componentes ao painel
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(0xE8E7E7)); // Cor do chat: #e8e7e7
        bottomPanel.add(statusLabel, BorderLayout.WEST);
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Ações dos botões e campos de texto
        messageField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            chatArea.append("Você: " + message + "\n\n");
            messageField.setText("");

            // Executa o script Python em uma thread separada
            new PythonExecutor(message, chatArea, statusLabel).execute();
        }
    }
}