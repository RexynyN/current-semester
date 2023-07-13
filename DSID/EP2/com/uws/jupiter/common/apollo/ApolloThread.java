package com.uws.jupiter.common.apollo;
/**
 * ApolloThread é uma classe para rodar os códigos Apollo em um contexto de Threads, podendo rodar múltiplos programas de uma vez.
 */
public class ApolloThread implements Runnable{
    private String program;
    private String [] args;
    private CodeResult result;


    public ApolloThread(String program, String[] args) {
        this.program = program;
        this.args = args;
    }

    @Override
    public void run() {
        this.result = Apollo.runCode(program, args);
    }
    
    public CodeResult getResult() {
        return result;
    }
}
