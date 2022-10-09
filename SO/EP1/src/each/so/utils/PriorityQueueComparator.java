package each.so.utils;

import each.so.bcp.BCP;
import java.util.Comparator;

// Comparador para a fila de prioridade
public class PriorityQueueComparator implements Comparator<BCP> {
    @Override
    public int compare(BCP b1, BCP b2) {
        return b1.retornaCreditos() < b2.retornaCreditos() ? 1 : -1;
    }
}