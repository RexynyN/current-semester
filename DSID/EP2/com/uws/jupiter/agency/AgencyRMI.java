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

public class AgencyRMI extends UnicastRemoteObject implements Agency {
    private List<Agent> agents = new ArrayList<Agent>(); 
    private final Host home;

    public AgencyRMI(Host home) throws RemoteException {
        super();
        this.home = home;
    }

    public void runAgent(String agentName, Agent agent, byte[] byteCodes) {
        System.out.println("Um novo agente chegou! Bem-vindo " + agent.getId());

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
        agent.onReturn();
    }

    @Override
    public void forwardMessage(String agentName, Message message) throws RemoteException {
        Agent receiver = null;
        for(Agent suspect : agents){
            if(suspect.getId() == agentName)
                receiver = suspect;
        }

        if(receiver == null)
            return;
        
        receiver.readMessage(message);
    }

    @Override
    public Agent createAgent(String type) throws RemoteException {
        if(type == "seeker"){
            String id = home.getName() + "-" + type + "-" + Utils.getRandomID();
            Seeker newSeeker = new Seeker(home, id);
            agents.add(newSeeker);
            return newSeeker;
        }

        String id = home.getName() + "-worker-" + Utils.getRandomID();
        Worker newWorker = new Worker(home, id);
        agents.add(newWorker);
        return newWorker;
    }
}

