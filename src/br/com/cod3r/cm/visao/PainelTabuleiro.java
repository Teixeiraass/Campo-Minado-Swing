package br.com.cod3r.cm.visao;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.GridLayout;

import br.com.cod3r.cm.modelo.Tabuleiro;

public class PainelTabuleiro extends JPanel{
    public PainelTabuleiro(Tabuleiro tabuleiro){
        setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));    //define como os componenetes visuais seram dispostos na tela. GridLayout precisa ser importado e ele divide os componentes em linhas e colunas

        tabuleiro.paraCada(c -> add(new BotaoCampo(c)));

        tabuleiro.registrarObservador(e -> {
            SwingUtilities.invokeLater(() -> {  //Esse SwingUltilities faz com que a resposta de ganhou ou perdeu so seja executada quando a interface inteira for atualizada
                if(e.isGanhou()){
                JOptionPane.showMessageDialog(this, "Você Ganhou!!");
                }else JOptionPane.showMessageDialog(this, "Você Perdeu :(");
                tabuleiro.reiniciar();
            });
        });
    }
}
