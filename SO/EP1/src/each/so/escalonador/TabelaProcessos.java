package each.so.escalonador;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import each.so.bcp.BCP;

// TABELA DE PROCESSOS
// Class que emula uma tabela de processos no S.O. com inserção, remoção e verificação nula
public class TabelaProcessos {

    private Map<UUID, BCP> processos = new HashMap<>();

    public void adicionaProcesso(BCP bcp) {
        processos.put(bcp.getRef(), bcp);
    }

    public void removeProcesso(UUID id) {
        processos.remove(id);
    }

    public boolean temProcesso() {
        return !processos.isEmpty();
    }
}
