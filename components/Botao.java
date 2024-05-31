package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Botao extends JButton implements ActionListener {

    public Botao(String texto) {
        super(texto);
        this.addActionListener(this);
        this.setFont(new Font("Arial", Font.BOLD, 14));
        this.setBackground(Color.LIGHT_GRAY);
        this.setForeground(Color.BLACK);
        this.setBorder(BorderFactory.createRaisedBevelBorder());
        this.setPreferredSize(new Dimension(150, 40));

        // Adiciona o efeito de hover
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Ações quando o mouse entra no botão
                Botao.this.setBackground(Color.GRAY); // Troca a cor de fundo
                Botao.this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Muda o cursor para a mão
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Ações quando o mouse sai do botão
                Botao.this.setBackground(Color.LIGHT_GRAY); // Volta para a cor original
                Botao.this.setCursor(Cursor.getDefaultCursor()); // Volta para o cursor padrão
            }
        });
    }

    
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // Executar o script Python
            String filePath = new File(".").getAbsolutePath() + "\\script.py";

            ProcessBuilder pb = new ProcessBuilder("python", filePath, "5", "11"); // "5 11" são os argumentos para o script
            Process process = pb.start(); 

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            
            StringBuilder buffer = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null){           
                buffer.append(line);
            }
            
            // Exibir o resultado no JOptionPane
            JOptionPane.showMessageDialog(null, "Value is: " + buffer.toString());

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao executar o script Python: " + ex.getMessage());
        }
    }
}