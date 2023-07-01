package com.usp.dsid.prototype;

/**
 * Um agente que procura uma agência que esteja rodando numa máquina com as especificações passadas
*/
public class Seeker extends Agent{

    public Seeker(Host home) {
        super(home);
    }

    @Override
    public void beforeDeparture() {
        throw new UnsupportedOperationException("Unimplemented method 'beforeDeparture'");
    }

    @Override
    public void onArrival() {
        throw new UnsupportedOperationException("Unimplemented method 'onArrival'");
    }

    @Override
    public void onReturn() {
        throw new UnsupportedOperationException("Unimplemented method 'onReturn'");
    }
    
}
