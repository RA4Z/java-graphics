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
    private final JTextArea chatArea;
    private final JLabel statusLabel;

    public PythonExecutor(String message, JTextArea chatArea, JLabel statusLabel) {
        this.message = message;
        this.chatArea = chatArea;
        this.statusLabel = statusLabel;
    }

    @Override
    protected Void doInBackground() throws Exception {
        try {
            publish("Enviando mensagem...");

            // URL do seu servidor Flask
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
                response.append(line);
            }
            reader.close();

            // Processar a resposta (assuma que é JSON)
            // ... (ajuste de acordo com a estrutura da resposta) ...
            publish(response.toString()); // Publica a resposta para ser exibida

        } catch (Exception ex) {
            throw new RuntimeException("Erro ao comunicar com o servidor Flask: " + ex.getMessage());
        }
        return null;
    }

    @Override
    protected void process(java.util.List<String> chunks) {
        for (String chunk : chunks) {
            if (chunk.equals("Enviando mensagem...")) {
                statusLabel.setText(chunk);
            } else {
                // Processar a resposta JSON aqui
                // ...
                // Exemplo: assumindo que a resposta é apenas uma string
                chatArea.append("Gemini: " + chunk + "\n\n"); 
            }
        }
    }

    @Override
    protected void done() {
        statusLabel.setText("");
    }
}