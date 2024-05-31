import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChatGUI extends JFrame {

    private JTextArea chatArea;
    private JTextField messageField;
    private JLabel statusLabel; // Rótulo para exibir o status
    private JButton sendButton;
    private String result = ""; // Variável para armazenar o resultado do script Python

    public ChatGUI() {
        // Configura a janela principal
        setTitle("Chat");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Define a cor de fundo da janela
        getContentPane().setBackground(new Color(0xE8E7E7)); // Cor do chat: #e8e7e7

        // Cria os componentes da interface
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setBackground(new Color(0x176B87)); // Cor de fundo do output: #176b87
        chatArea.setForeground(Color.WHITE);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));
        chatArea.setLineWrap(true); // Habilita a quebra de linha
        chatArea.setWrapStyleWord(true); // Quebra a linha nas palavras

        messageField = new JTextField();
        messageField.setBackground(new Color(0x053B50)); // Cor de fundo do input: #053b50
        messageField.setForeground(Color.WHITE);
        messageField.setFont(new Font("Arial", Font.PLAIN, 14));

        sendButton = new JButton("Enviar");
        sendButton.setBackground(new Color(0x0C2D48));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // Cria o rótulo de status
        statusLabel = new JLabel("");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT); // Alinha o texto à esquerda
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setForeground(Color.GRAY);

        // Adiciona um KeyListener ao campo de entrada
        messageField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Se a tecla pressionada for Enter, envia a mensagem
                    String message = messageField.getText();
                    if (!message.isEmpty()) {
                        chatArea.append("Você: " + message + "\n\n"); // Adiciona uma linha em branco
                        messageField.setText("");

                        // Executa o script Python com a mensagem como argumento
                        executePythonScript(message);

                        // Adiciona o resultado do script ao chat
                        if (!result.isEmpty()) {
                            chatArea.append("Gemini: " + result + "\n\n");
                            result = ""; // Limpa a variável result
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        // Define a ação do botão "Enviar"
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText();
                if (!message.isEmpty()) {
                    chatArea.append("Você: " + message + "\n\n"); // Adiciona uma linha em branco
                    messageField.setText("");

                    // Executa o script Python com a mensagem como argumento
                    executePythonScript(message);

                    // Adiciona o resultado do script ao chat
                    if (!result.isEmpty()) {
                        chatArea.append("Gemini: " + result + "\n\n");
                        result = ""; // Limpa a variável result
                    }
                }
            }
        });

        // Cria o layout da interface
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(new Color(0xE8E7E7)); // Cor do chat: #e8e7e7

        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBackground(new Color(0xE8E7E7)); // Cor do chat: #e8e7e7
        scrollPane.setForeground(Color.WHITE);

        // Ajusta o tamanho do JTextArea para ocupar toda a largura da janela
        chatArea.setSize(getContentPane().getWidth(), chatArea.getHeight());

        JPanel bottomPanel = new JPanel(new BorderLayout()); // Usa BorderLayout no painel inferior
        bottomPanel.setBackground(new Color(0xE8E7E7)); // Cor do chat: #e8e7e7
        bottomPanel.add(messageField, BorderLayout.CENTER); // Adiciona o campo de entrada ao centro
        bottomPanel.add(sendButton, BorderLayout.EAST); // Adiciona o botão à direita

        // Adiciona o rótulo de status ao painel inferior
        bottomPanel.add(statusLabel, BorderLayout.WEST);

        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        // Adiciona o conteúdo à janela
        setContentPane(contentPane);
        setVisible(true);
    }

    // Função para executar o script Python
    private void executePythonScript(String message) {
        // Desabilita o campo de mensagem e o botão Enviar
        messageField.setEnabled(false);
        sendButton.setEnabled(false);

        statusLabel.setText("Processando...");
        // Cria e inicia uma nova thread para executar o script Python
        Thread thread = new Thread(() -> {
            try {
                // Executar o script Python
                String filePath = new File(".").getAbsolutePath() + "\\scripts\\Gemini\\gemini.py";
                ProcessBuilder pb = new ProcessBuilder("python", filePath, message);
                Process process = pb.start();

                // Ler a saída do Processo
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder buffer = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                reader.close();

                // Verificar o código de saída do processo
                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    // Erro na execução do script Python
                    statusLabel.setText(""); // Volta para espaço vazio
                    String errorMessage = "Erro ao executar o script Python (código de saída: " + exitCode + ")";
                    JOptionPane.showMessageDialog(null, errorMessage);
                    return;
                }

                // Atualiza a área de chat com a resposta do script
                SwingUtilities.invokeLater(() -> {
                    result = buffer.toString().replace("\\n", "\n");
                    if (!result.isEmpty()) {
                        chatArea.append("Gemini: " + result + "\n\n");
                        result = "";
                    }
                    // Limpa a mensagem de status
                    statusLabel.setText(""); // Volta para espaço vazio
                });
            } catch (IOException | InterruptedException ex) {
                statusLabel.setText(""); // Volta para espaço vazio
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao executar o script Python: " + ex.getMessage());
                // Limpa a mensagem de status em caso de erro
            } finally {
                // Habilita o campo de mensagem e o botão Enviar após a execução do script
                SwingUtilities.invokeLater(() -> {
                    messageField.setEnabled(true);
                    sendButton.setEnabled(true);
                });
            }
        });
        thread.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatGUI());
    }
}