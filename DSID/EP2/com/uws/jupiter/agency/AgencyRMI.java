package com.uws.jupiter.agency;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import com.uws.jupiter.common.Host;
import com.uws.jupiter.common.Message;
import com.uws.jupiter.common.Sandbox;
import com.uws.jupiter.common.Utils;
import com.uws.jupiter.common.agents.Agent;
import com.uws.jupiter.common.agents.Seeker;
import com.uws.jupiter.common.agents.Worker;
import com.uws.jupiter.nameserver.LookupServer;

public class AgencyRMI extends UnicastRemoteObject implements Agency {
    private List<Agent> agents = new ArrayList<Agent>(); 
    private final Host home;

    public AgencyRMI(Host home) throws RemoteException {
        super();
        this.home = home;
    }


    @Override
    public void runAgent(String agentName, Agent agent, byte[] byteCodes) {
        Utils.infoPrint("Um novo agente chegou! Bem-vindo " + agent.getId());

        try {
            // Run the Agent in its own Sandbox
            Sandbox sandbox = new Sandbox(agent);
            sandbox.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void returnHome(String agentName, Agent agent, byte[] byteCodes) throws RemoteException {
        String [] name = agent.getName().split(".");
        System.out.println("Um " + name[name.length - 1] + " acabou de chegar!");
        agents.add(agent);
        // agent.onReturn();
    }

    @Override
    public void forwardMessage(String agentName, Message message) throws RemoteException {
        System.out.println("Mensagem Recebida!");
        for(Agent suspect : agents){
            if(suspect.getId().equals(agentName))
                suspect.readMessage(message);
        }
    }

    @Override
    public Seeker createSeeker() throws RemoteException {
        String id = "seeker-" + home.getName() + "-" + Utils.getRandomID();
        Seeker newSeeker = new Seeker(home, id);
        agents.add(newSeeker);
        return newSeeker;
    }

    @Override
    public Worker createWorker() throws RemoteException {
        String id = "worker-" + home.getName() + "-" + Utils.getRandomID();
        Worker newWorker = new Worker(home, id);
        agentRegister(newWorker);
        agents.add(newWorker);
        return newWorker;
    }

    private void agentRegister(Agent agent) {
        try {
            LookupServer ns = Utils.connectNameServer();
            ns.registerAgent(agent);
        } catch (Exception e) {
            Utils.failPrint("Não foi possível registrar o agente.");
        }
    }
}

