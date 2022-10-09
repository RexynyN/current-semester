package each.so.processo;

public enum Instrucao {
    COM("COM"), SAIDA("SAIDA"), ES("E/S"), X("X="), Y("Y=");
    String instrucao;

    Instrucao(String instrucao){
        this.instrucao = instrucao;
    }

    // Transforma uma string em enum (no caso, o comando a ser executado)
    static Instrucao fromString(String instrucao){
        for (Instrucao i: values()) {
            if(instrucao.startsWith(i.instrucao)){
                return i;
            }
        }
        return null;
    }
}
