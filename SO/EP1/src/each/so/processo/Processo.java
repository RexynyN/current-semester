package each.so.processo;

import java.util.List;

import each.so.utils.Logger;

public class Processo {
    private String nome;
    // ESTADO
    private Estado estado = Estado.PRONTO;
    // REGISTRADORES DE USO GERAL
    private int registradorX = 0;
    private int registradorY = 0;
    private int quantumBloqueio = 0;
    // PRIORIDADE
    private int prioridade = 0;
    // CRÉDITOS DO PROCESSO
    private int creditos = 0;
    // REFERÊNCIA AO SEGMENTO DE TEXTO
    private final List<String> instrucoes;

    public Processo(String nome, int prioridade, List<String> instrucoes) {
        this.prioridade = prioridade;
        this.creditos = prioridade;
        this.nome = nome;
        this.instrucoes = instrucoes;
    }

    //Funcao que interpreta as instrucoes recebidas e modifica o processo
    private void rodarComando(String instrucao){
        switch(Instrucao.fromString(instrucao)){
            case COM:
                break;
            case ES:
                Logger.logln("E/S Iniciada por " + nome );
                this.estado = Estado.BLOQUEADO;
                this.quantumBloqueio = 2;
                break;
            case X:
                this.registradorX = Integer.parseInt(instrucao.split("=")[1]);
                break;
            case Y:
                this.registradorY = Integer.parseInt(instrucao.split("=")[1]);
                break;
            case SAIDA:
                this.estado = Estado.FINALIZADO;
                break;
        }
    }

    // Funcao que recupera a instrucao e inicia o processo de interpreta-lo
    public void executa(int contador){
        String instrucao = instrucoes.get(contador);
        this.estado = Estado.EXECUTANDO;
        rodarComando(instrucao);
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public int getQuantumBloqueio() {
        return quantumBloqueio;
    }

    // Quantuns bloqueados
    public void decrementaQuantunsBloqueados(){
        this.quantumBloqueio--;
    }

    public int getX (){
        return this.registradorX;
    }

    public int getY(){
        return this.registradorY;
    }

    public int getPrioridade(){
        return this.prioridade;
    }

    public void decrementaCreditos() {
        this.creditos--;
    }

    public void resetarCreditos(){
        this.creditos = this.prioridade;
    }

    @Override
    public String toString() {
        return "Processo(" + estado +
                ", X=" + registradorX +
                ", Y=" + registradorY+
                ")";
    }

    public int getCreditos() {
        return this.creditos;
    }

}
