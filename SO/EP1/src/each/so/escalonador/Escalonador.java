package each.so.escalonador;

import static each.so.processo.Estado.*;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import each.so.bcp.BCP;
import each.so.processo.Estado;
import each.so.utils.Logger;
import each.so.utils.PriorityQueueComparator;
import each.so.utils.Report;

public class Escalonador {
    private Report report;
    private final Integer quantum;
    // PROCESSOS PRONTOS
    private final Queue<BCP> prontos = new PriorityQueue<BCP>(new PriorityQueueComparator());
    // PROCESSOS BLOQUEADOS
    private final Queue<BCP> bloqueados = new LinkedList<>();
    private final TabelaProcessos tabelaProcessos = new TabelaProcessos();

    public Escalonador(Report report, Integer quantum) {
        this.report = report;
        this.quantum = quantum;
    }

    // Carrega o BCP na tabela de processos e adiciona na fila de prontos
    public void carregaBCP(BCP blocoComandos) {
        tabelaProcessos.adicionaProcesso(blocoComandos);
        prontos.add(blocoComandos);
    }

    // Inicia o escalonador
    public void inicia() {
        listarProcessos();
        while (tabelaProcessos.temProcesso()) {
            this.bloqueados.forEach(BCP::decrementaQuantum);
            if (!prontos.isEmpty()) {
                escalonaProcesso();
            }

            if (!this.bloqueados.isEmpty()) {
                verificaBloqueados();
            }
        }
    }

    // Dá o log de carregamento dos processos em ordem de prioridade
    private void listarProcessos() {
        Queue<BCP> printer = new PriorityQueue<>(prontos);
        while (!printer.isEmpty()) {
            BCP b = printer.poll();
            Logger.logln("Carregando " + b.getNomePrograma() + " - Prioridade=" + b.retornaPrioridade());
        }
    }

    // Trata a fila de processos bloqueados
    private void verificaBloqueados() {
        BCP bcpBloqueado = this.bloqueados.peek();

        if (bcpBloqueado != null && bcpBloqueado.verificaPronto()) {
            BCP pronto = this.bloqueados.poll();
            this.prontos.offer(pronto);
        }
    }


    // Executa 1 quantum para o proximo processo da fila (onde a mágica acontece)
    private void escalonaProcesso() {
        // Retorna o próximo processo na fila
        BCP bcp = prontos.poll();
        Estado estado = null;

        // Decrementa os créditos e checa se todos os processos têm zero créditos
        if (bcp.retornaCreditos() == 0) {
            prontos.offer(bcp);

            if (checarTodosZeros()) {
                Logger.logln("Todos os processos possuem zero créditos, resetando créditos.");
                resetarCreditos();
            }

            return;
        }

        // Decrementa os créditos do processo
        Logger.logln("Executando " + bcp.getNomePrograma() + " - Créditos=" + bcp.retornaCreditos());
        bcp.decrementaCreditos();

        // Simula um quantum (n_com) de execução
        for (int i = 0; i < quantum; i++) {
            estado = bcp.executaProxInstrucao();

            // Se não está executando, foi interrompido
            if (!EXECUTANDO.equals(estado))
                Logger.logln("Interrompendo " + bcp.getNomePrograma() + " após " + (i + 1) + " instruções.");

            // Se o processo for finalizado ou bloqueado, sai do quantum e trata do processo
            if (FINALIZADO.equals(estado)) {
                // Remove da tabela (processo terminado) e conta as instruções
                tabelaProcessos.removeProcesso(bcp.getRef());
                report.instrucoesPorQuantum(i + 1);
                Logger.logln(bcp.retornaProcessoFinalizado());
                break;
            } else if (BLOQUEADO.equals(estado)) {
                // Coloca na fila de bloqueados e conta as instruções
                bloqueados.offer(bcp);
                report.instrucoesPorQuantum(i + 1);
                break;
            }
        }

        // Coloca na fila de prontos e conta as instruções
        if (EXECUTANDO.equals(estado)) {
            report.instrucoesPorQuantum(quantum);
            prontos.offer(bcp);
            bcp.trocaEstado(PRONTO);
            Logger.logln("Interrompendo " + bcp.getNomePrograma() + " após " + quantum + " instruções.");
        }

        // Conta troca de processos
        report.incrementaTrocasProcesso();
    }

    // Reseta os créditos para o seu número de prioridade
    private void resetarCreditos() {
        for (BCP b : prontos)
            b.resetarCreditos();

        for (BCP b : bloqueados)
            b.resetarCreditos();
    }

    // Checa se TODOS os processos tem zero créditos
    private boolean checarTodosZeros() {
        boolean zeros = true;
        for (BCP b : prontos) {
            if (b.retornaCreditos() > 0) {
                zeros = false;
                return zeros;
            }
        }

        for (BCP b : bloqueados) {
            if (b.retornaCreditos() > 0) {
                zeros = false;
                return zeros;
            }
        }

        return zeros;
    }
}
