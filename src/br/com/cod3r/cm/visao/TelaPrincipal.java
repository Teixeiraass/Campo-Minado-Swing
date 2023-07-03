package br.com.cod3r.cm.visao;

import javax.swing.JFrame;

import br.com.cod3r.cm.modelo.Tabuleiro;

public class TelaPrincipal extends JFrame{

    public TelaPrincipal(){
        Tabuleiro tabuleiro = new Tabuleiro(16, 30, 30);
        PainelTabuleiro painelTabuleiro = new PainelTabuleiro(tabuleiro);

        add(painelTabuleiro); //add adiciona o painelTabuleiro na tela(interface)
    
        setVisible(true);
        setTitle("Campo Minado");
        setSize(690, 438);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        new TelaPrincipal();
    }
}
