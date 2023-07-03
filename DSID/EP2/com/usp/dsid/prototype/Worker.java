package com.usp.dsid.prototype;

import com.usp.dsid.common.Utils;
import com.usp.dsid.common.apollo.Apollo;
import com.usp.dsid.common.apollo.CodeResult;

public class Worker extends Agent {
    private final String payload;
    private final String[] args;
    private CodeResult result;
    private Host targetMachine;

    /**
     * Um agente que carrega código para ser executado
    */
    public Worker(Host home, String id, String codePath, String[] args) {
        super(home, id);
        this.payload = Utils.readFile(codePath);
        this.args = args;
    }

    @Override
    public void beforeDeparture() {
        System.out.println(getId() + " saindo em uma aventura!");
    }

    @Override
    public void onArrival(Host host) {
        System.out.println("Olá " + host.getName() + "! Estou rodando aqui!");
        result = Apollo.runCode(payload, args);
    }

    @Override
    public void onReturn() {
        // CodeResult result = Apollo.runCode(program, argus);
        if (result.hadErrors()) {
            String compilationErrors = result.getCompilationErrors();
            System.out.println(compilationErrors);
        } else {
            System.out.println(result.getLogs());
            System.out.println(result.getReturnValue());
        }
    }

    @Override
    public void readMessage(Message msg) {
        switch(msg.getMatter()){
            case "found": {

            }

            case "notfound": {
                System.out.println();
            }
        }
    }

    @Override
    public void sendMessage(Host receiver, Message msg) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendMessage'");
    }
}
