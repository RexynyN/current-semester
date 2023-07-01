package com.usp.dsid.prototype;

import com.usp.dsid.common.Utils;
import com.usp.dsid.common.apollo.Apollo;
import com.usp.dsid.common.apollo.CodeResult;

public class Worker extends Agent {
    private final String payload;
    private final String[] args;
    private CodeResult result;

    /**
     * Um agente que carrega código para ser executado
    */
    public Worker(Host home, String codePath, String[] args) {
        super(home);
        this.payload = Utils.readFile(codePath);
        this.args = args;
    }

    @Override
    public void beforeDeparture() {
        // System.out.println("TeeHee :P");
    }

    @Override
    public void onArrival() {
        result = Apollo.runCode(payload, args);
    }

    @Override
    public void onReturn() {
        System.out.println(result.getLogs());
        System.out.println(result.getReturnValue());
    }
}
