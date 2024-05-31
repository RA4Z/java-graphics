package chatgui;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

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
            // Desabilita o campo de mensagem e o botão Enviar
            publish("Enviando mensagem...");

            // Executar o script Python
            String filePath = new File(".").getAbsolutePath() + "\\scripts\\Gemini\\gemini.py";
            ProcessBuilder pb = new ProcessBuilder("python", filePath, message);
            Process process = pb.start();

            // Ler a saída do Processo
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();

            // Verificar o código de saída do processo
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Erro ao executar o script Python (código de saída: " + exitCode + ")");
            }

            // Retorna o resultado
            publish(buffer.toString().replace("\\n", "\n"));

        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException("Erro ao executar o script Python: " + ex.getMessage());
        }
        return null;
    }

    @Override
    protected void process(java.util.List<String> chunks) {
        for (String chunk : chunks) {
            if (chunk.equals("Enviando mensagem...")) {
                statusLabel.setText(chunk);
            } else {
                chatArea.append("Gemini: " + chunk + "\n\n");
            }
        }
    }

    @Override
    protected void done() {
        statusLabel.setText(""); // Volta para espaço vazio
    }
}