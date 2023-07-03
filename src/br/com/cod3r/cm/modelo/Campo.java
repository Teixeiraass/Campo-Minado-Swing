package br.com.cod3r.cm.modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo {
    
    private boolean minado = false;
    private boolean aberto = false;
    private boolean marcado = false;

    private final int LINHA;
    private final int COLUNA;

    private List<Campo> vizinhos = new ArrayList<>();
    private List<CampoObservador> observadores = new ArrayList<>();

    Campo(int LINHA, int COLUNA){
        this.LINHA = LINHA;
        this.COLUNA = COLUNA;
    }

    public void registrarObservador(CampoObservador observador){
        observadores.add(observador);
    }

    private void notificarObservadores(CampoEvento evento){
        observadores.stream().forEach(obs -> obs.eventoOcorreu(this, evento));  //Executa o metodo evento ocorreu para cada posição da lista
    }

    //Esse metodo cria os vizinhos
    boolean adicionarVizinho(Campo vizinho){

        //Esses 3 booleans servem para garantir que nao vai ser adicionado vizinho na msm LINHA e COLUNA que foi instanciado o campo
        boolean LINHADiferente = LINHA != vizinho.LINHA;    //Se a LINHA que foi dada no construtor for diferente da passada no parametro entao sao LINHAs diferentes
        boolean COLUNADiferente = COLUNA != vizinho.COLUNA; //Se a COLUNA passada no contrutor for diferente da passada no parametro, as COLUNAs sao diferentes
        boolean diagonal = LINHADiferente && COLUNADiferente;   //Caso a COLUNA e a LINHA forem diferentes entao diagonal vira true, ou seja tem diagonal

        //Esse 3 int retorna o delta
        int deltaLINHA = Math.abs(LINHA - vizinho.LINHA);
        int deltaCOLUNA = Math.abs(COLUNA - vizinho.COLUNA);
        int deltaGeral = deltaCOLUNA + deltaLINHA;

        //Caso o deltaGeral seja 1 e  nao tenha diagonal vai ser adicionado um vizinho
        if(deltaGeral == 1 && !diagonal){
            vizinhos.add(vizinho);
            return true;
        }else if(deltaGeral == 2 && diagonal){ //Caso o delta seja dois e tenha diagonal tambem adiciona um vizinho
            vizinhos.add(vizinho);
            return true;
        }else return false; //Se nao atender os requisitos significa que o valor passado no parametro na oe vizinho, ou seja nao esta perto do valor passado no contrutor
    }

    //So alterna se ta marcado com a bandeirinha ou nao
    public void alternarMarcacao(){
        if(!aberto){
            marcado = !marcado;

            if(marcado){
                notificarObservadores(CampoEvento.MARCAR);  //Se estiver marcado ele notifica os observadores que esta marcado
            }else{
                notificarObservadores(CampoEvento.DESMARCAR);
            }
        }
    }

    public boolean abrir(){
        if(!aberto && !marcado){
            //se o campo aberto estiver minado lança uma exceção
            if(minado){
                // TODO implemetar nova versão
                notificarObservadores(CampoEvento.EXPLODIR);    //Caso tenha uma mina ele gera o evento de explodir pro observador
                return true;
            }
            
            setAberto(true);  //Abre e ja manda para o observador que esta aberto

            //se a vizinhaça for segura ele faz um loop, pega o valor desse loop e usa recursividade para ir abrindo esse valor
            if(vizinhancaSegura()){
                vizinhos.forEach(v -> v.abrir());
            }
            return true;
        }
        return false;
    }

    //Checa se nao tem bomba na vizinhanca
    public boolean vizinhancaSegura(){
        return vizinhos.stream().noneMatch(v -> v.minado); //ve se na array dos vizinhos nao tem nenhum valor minado nele, se tiver alguma mina nos vizinhos ele nao abre esse vizinho
    }

    void minar(){
        if(!minado){
            minado = true;
        }
    }

    public boolean isMarcado(){
        return marcado;
    }

    public boolean isAberto(){
        return aberto;
    }

    void setAberto(boolean aberto){
        this.aberto = aberto;

        if(aberto){
            notificarObservadores(CampoEvento.ABRIR); //Caso nao tenha uma mina ele gera o evento de abrir pro observador
        }
    }

    public boolean isMinado(){
        return minado;
    }

    public int getLinha(){
        return LINHA;
    }

    public int getColuna(){
        return COLUNA;
    }

    boolean objetivoAlcançado(){
        boolean desvendado = !minado && aberto;
        boolean protegido = minado && marcado;

        return desvendado || protegido;
    }

    public int minasNaVizinhanças(){
        return (int) vizinhos.stream().filter(v -> v.minado).count(); //Esse metodo pega a array vizinhos, e usa o filter para filtrar os vizinhos que estao minados, usa o cout para contar quantos vizinhos tem minado na vizinhança
    }

    void reiniciar(){
        aberto = false;
        minado = false;
        marcado = false;
        notificarObservadores(CampoEvento.REINICIAR);
    }

}
