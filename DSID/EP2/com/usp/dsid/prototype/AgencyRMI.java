package com.usp.dsid.prototype;

import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class AgencyRMI extends UnicastRemoteObject implements Agency {
    private List<Agent> agents = new ArrayList<Agent>(); 
    private Agency currentAgency;

    public AgencyRMI() throws RemoteException {
        super();
    }

    public void runAgent(String agentName, Agent agent, byte[] byteCodes) {
        System.out.println("Um novo agente chegou! Bem-vindo " + agentName);

        try {
            // Save the class on the local file system
            FileOutputStream out = new FileOutputStream(agentName + ".class");
            out.write(byteCodes); 

            // Run the Agent in its own Sandbox
            Sandbox sandbox = new Sandbox(agent);
            sandbox.start();
            out.close();
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
    public void sendMessage(String agentName, Message message) throws RemoteException {
        
    }

    @Override
    public void createSeeker() throws RemoteException {
    
    }

    
}

