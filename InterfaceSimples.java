// Sua classe principal (InterfaceSimples.java)
import javax.swing.*;

import components.Botao;

import java.awt.*;

public class InterfaceSimples {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Aprendizagem gráfica");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JPanel painel = new JPanel();
        painel.setLayout(new FlowLayout());

        JLabel label = new JLabel("Minha Primeira Interface Gráfica em JAVA!");

        // Cria o botão utilizando a classe Botao
        Botao botao = new Botao("Clique Aqui"); 

        painel.add(label);
        painel.add(botao);

        frame.add(painel);
        frame.setVisible(true);
    }
}