package each.so.bcp;

import java.util.List;
import java.util.UUID;

import each.so.processo.Estado;
import each.so.processo.Processo;
import each.so.utils.Logger;

public class BCP{
    private final String nomePrograma;
    // CONTADOR DE PROGRAMA
    private int contadorPrograma;
    private final Processo processo;
    private final UUID ref;

    // Metodo estático que retorna um objeto BCP inicializado
    public static BCP parse(List<String> instrucoes, String prioridade) {
        String nome = instrucoes.remove(0);
        return new BCP(nome, new Processo(nome, Integer.parseInt(prioridade), instrucoes));
    }

    private BCP(String nome, Processo processo) {
        this.nomePrograma = nome;
        this.processo = processo;
        this.contadorPrograma = 0;
        this.ref = UUID.randomUUID();
    }

    public String getNomePrograma() {
        return nomePrograma;
    }

    public UUID getRef() {
        return ref;
    }

    public Estado executaProxInstrucao() {
        this.processo.executa(this.contadorPrograma);
        this.contadorPrograma++;

        return this.processo.getEstado();
    }

    public void trocaEstado(Estado estado) {
        this.processo.setEstado(estado);
    }

    public boolean verificaPronto() {
        if (this.processo.getQuantumBloqueio() == 0) {
            this.trocaEstado(Estado.PRONTO);
            return true;
        }
        return false;
    }

    public void decrementaQuantum() {
        this.processo.decrementaQuantunsBloqueados();
    }

    public void resetarCreditos(){
        this.processo.resetarCreditos();
    }

    public int decrementaCreditos() {
        if (this.processo.getCreditos() > 0){
            this.processo.decrementaCreditos();
            return this.processo.getCreditos();
        }

        return 0;
    }

    public int retornaPrioridade() {
        return this.processo.getPrioridade();
    }

    public int retornaCreditos() {
        return this.processo.getCreditos();
    }

    public String retornaProcessoFinalizado() {
        return getNomePrograma() + " terminado. X=" + this.processo.getX() + ". Y=" + this.processo.getY();
    }
}
