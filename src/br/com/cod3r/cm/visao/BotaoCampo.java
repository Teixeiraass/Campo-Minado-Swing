package br.com.cod3r.cm.visao;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import br.com.cod3r.cm.modelo.Campo;
import br.com.cod3r.cm.modelo.CampoEvento;
import br.com.cod3r.cm.modelo.CampoObservador;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BotaoCampo extends JButton implements CampoObservador, MouseListener{ //ele precisa ser notifica sempre que acontecer uma ação

    private Campo campo;
    private final Color BG_PADRAO = new Color(184, 184, 184);   //criar cores em java
    private final Color BG_MARCADO = new Color(8, 179, 247);
    private final Color BG_EXPLODIR = new Color(189, 66, 68);
    private final Color TEXTO_VERDE = new Color(0, 100, 0);

    public BotaoCampo(Campo campo){
        this.campo = campo;
        setBorder(BorderFactory.createBevelBorder(0));  //Seta a  borda para ficar mais parecida com a do campo minado
        setBackground(BG_PADRAO);   //Configura as cor do botao
        setOpaque(true);

        addMouseListener(this); //Registrar evento do mouse, aqui falo que eu quero adicionar o mouseListener dessa classe
        campo.registrarObservador(this);    //Aqui ele recebe um campo, e no metodo de registrar no campo ele registra essa classe, por que ela vai aguardar o observador passar a informação
    }

    @Override
    public void eventoOcorreu(Campo c, CampoEvento e) {
       switch(e){
        case ABRIR:
            aplicarEstiloAbrir();
            break;
        case MARCAR:
            aplicarEstiloMarcar();
            break;
        case EXPLODIR:
            aplicarEstiloExplodir();
            break;
        default:
            aplicarEstiloPadrao();
       }

       SwingUtilities.invokeLater(() -> {   //Garante que nao tera problema de renderização
        repaint();
        validate();
       });
    }

    private void aplicarEstiloPadrao() {
        setBackground(BG_PADRAO);
        setText("");
        setBorder(BorderFactory.createBevelBorder(0));
    }

    private void aplicarEstiloExplodir() {
        setBackground(BG_EXPLODIR);
        setForeground(Color.WHITE);
        setText("X");
    }

    private void aplicarEstiloMarcar() {
        setBackground(BG_MARCADO);
        setForeground(Color.BLACK);
        setText("M");
    }

    //Define como vai ser o campo quando abrir
    private void aplicarEstiloAbrir() {
        setBorder(BorderFactory.createLineBorder(Color.GRAY)); //Borda do campo 
        
        if(campo.isMinado()){
            setBackground(BG_EXPLODIR);
            return;
        }

        setBackground(BG_PADRAO); //Cor do campo

        switch(campo.minasNaVizinhanças()){
            case 1:
                setForeground(TEXTO_VERDE);
                break;
            case 2:
                setForeground(Color.BLUE);
                break;
            case 3:
                setForeground(Color.YELLOW);
                break;
            case 4:
            case 5:
            case 6:
                setForeground(Color.RED);
                break;
            default:
                setForeground(Color.PINK);
        }
        String valor = !campo.vizinhancaSegura() ? campo.minasNaVizinhanças() + "" : ""; //caso a vizinhança nao seja segura, ele vai retornar o metodo minasVizinhanca que retorna o numero de minas ao redor, "" para tranformar em string

        setText(valor); //Seta um texto, esse texto vai ser o valor gerado
    }

    //Interface dos eventos do mouse
    
    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == 1){
            campo.abrir();
        }else{
            campo.alternarMarcacao();
        }
    }
    
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
