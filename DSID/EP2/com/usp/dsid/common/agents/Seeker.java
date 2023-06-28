package com.usp.dsid.common.agents;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.Map;

public class Seeker extends UnicastRemoteObject implements Agent {
    private String name;
    private Registry nameServer;

    public Seeker(String name, Registry server) throws RemoteException{
        this.nameServer = server;
    }

    public Object receiveMessage (String message, Object arg){
        switch(message){
            case "seekAgency": {
                return seekAgency((Map<String, String>) arg);
            }

            case "sendAgent": {
                sendAgent((Agent) arg);
                return null;
            }

            case "retrieveAgent": {
                return (Object) retrieveAgent((String) arg);
            }

            default: {
                return null;
            }
        }
    }

    // Procura uma agência com as especificações passadas
    private String seekAgency(Map<String, String> specs){
        return "id";
    }

    // Faz a migração do agente para outra máquina
    private void sendAgent(Agent agent){

    }

    // Checa se o agente terminou de rodar na agência e o retorna
    private Agent retrieveAgent(String id){
        return new Worker("");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return "seeker";
    }
}
