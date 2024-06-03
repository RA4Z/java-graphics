package chatgui;

import javax.swing.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class PythonExecutor extends SwingWorker<Void, String> {

    private final String message;
    private final JTextPane chatArea; // Mudança: JTextPane
    private final JLabel statusLabel;
    private final JTextField messageField;
    private final JButton sendButton;
    private StringBuilder messageHistory;

    public PythonExecutor(String message, JTextPane chatArea, JLabel statusLabel, JTextField messageField,
            JButton sendButton, StringBuilder messageHistory) {
        this.message = message;
        this.chatArea = chatArea;
        this.statusLabel = statusLabel;
        this.messageField = messageField;
        this.sendButton = sendButton;
        this.messageHistory = messageHistory;
    }

    @Override
    protected Void doInBackground() throws Exception {
        try {
            this.messageField.setEnabled(false);
            this.sendButton.setEnabled(false);

            publish("Enviando mensagem...");

            // URL do servidor Flask
            URL url = new URL("http://10.1.43.63:5000/gemini");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Enviar a mensagem como parâmetro
            String data = "message=" + URLEncoder.encode(message, "UTF-8");
            OutputStream output = connection.getOutputStream();
            output.write(data.getBytes("UTF-8"));
            output.flush();
            output.close();

            // Ler a resposta do servidor
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line + "<br>");
            }
            reader.close();
            System.out.println(response.toString());
            publish(response.toString()); // Publica a resposta para ser exibida

        } catch (Exception ex) {
            throw new RuntimeException("Erro ao comunicar com o servidor Flask: " + ex.getMessage());
        }

        this.messageField.setEnabled(true);
        this.sendButton.setEnabled(true);
        return null;
    }

    @Override
    protected void process(java.util.List<String> chunks) {
        for (String chunk : chunks) {
            if (chunk.equals("Enviando mensagem...")) {
                statusLabel.setText(chunk);
            } else {
                // Adiciona texto ao JTextPane com estilo HTML
                chatArea.setContentType("text/html");
                messageHistory.append("<span style=\"color:black\">" + chunk + "</span> <br>");

                try {
                    chatArea.setText(messageHistory.toString());

                } catch (Error e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void done() {
        statusLabel.setText("");
    }
}