package br.com.cod3r.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador{
    private final int LINHAS;
    private final int COLUNAS;   
    private final int MINAS;
    
    private final List<Campo> campos = new ArrayList<>();
    private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();

    public Tabuleiro(int LINHAS, int COLUNAS, int MINAS){
        this.LINHAS = LINHAS;
        this.COLUNAS = COLUNAS;
        this.MINAS = MINAS;

        gerarCampos();
        associarVizinhos();
        sortearMinas();
    }   

    public void paraCada(Consumer<Campo> funcao){
        campos.forEach(funcao);
    }

    public int getLinhas(){
        return LINHAS;
    }

    public int getColunas(){
        return COLUNAS;
    }

    public int getMinas(){
        return MINAS;
    }

    public void registrarObservador(Consumer<ResultadoEvento> observador){
        observadores.add(observador);
    }

    private void notificarObservadores(boolean resultado){
        observadores.stream().forEach(obs -> obs.accept(new ResultadoEvento(resultado)));  //Executa o metodo evento ocorreu para cada posição da lista
    }

    public void abrirCampo(int linha, int coluna){
        campos.parallelStream()
        .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
        .findFirst()   //vai retornar o primeiro valor que encontrou
        .ifPresent(c -> c.abrir());
    }
    
    public void MarcarCampo(int linha, int coluna){
        campos.parallelStream()
        .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
        .findFirst()   //vai retornar o primeiro valor que encontrou
        .ifPresent(c -> c.alternarMarcacao());
    }

    //Esse metodo cria as LINHAS e COLUNAS com o for, entao vai ser adicionado na array Campo 
    private void gerarCampos() {
        for(int l = 0; l < LINHAS; l++){
            for(int c = 0; c < COLUNAS; c++){
                Campo campo = new Campo(l, c);
                campo.registrarObservador(this);
                campos.add(campo);
            }
        }
    }

    //esse for vai percorrer a lista duas vezes para associar os vizinhos, o c1 passa pelos indices da array como se fosse o i, entao o j passa a lista toda adicionando vizinhos para o indice que esta em i, e assim por diante
    private void associarVizinhos() {
        for(Campo c1: campos){  
            for(Campo c2: campos){
                c1.adicionarVizinho(c2);
            }
        }
    }


    private void sortearMinas() {
        long MINASArmadas = 0;

        Predicate<Campo> minado = c -> c.isMinado();

        do{
            int aleatorio = (int) (Math.random() * campos.size()); //Cria o numero aleatorio
            campos.get(aleatorio).minar(); //aqui ele usa o get no indice aleatorio, e mina esse indice
            MINASArmadas = campos.stream().filter(minado).count(); //Faz a verificação de campos minados, ele usa o filter para filtrar quais parametros estao com MINAS e usa o count para contar quantos tem, no inicio vai ser sempre 0 pq nao tera mina no campo
        }while(MINASArmadas < MINAS);
    }

    public boolean objetivoAlcançado(){
        return campos.stream().allMatch(c -> c.objetivoAlcançado()); //Aqui ele verifica se todos os campo dao match, no objetivo alcaçado
    }

    public void reiniciar(){
        campos.stream().forEach(c -> c.reiniciar()); //Aqui ele passa por toda a lista e reinicia todos os campos com o metodo reinciar que foi criado na classe campo 
        sortearMinas();
    }

    @Override
    public void eventoOcorreu(Campo c, CampoEvento e) {
        if(e == CampoEvento.EXPLODIR){
            mostrarMinas();
            notificarObservadores(false);
        }else if(objetivoAlcançado()){
            System.out.println("Ganhou");
            notificarObservadores(true);
        }
    }

    private void mostrarMinas(){
        campos.stream()
        .filter(c -> c.isMinado())
        .filter(c -> !c.isMarcado())
        .forEach(c -> c.setAberto(true));
    }
    
}