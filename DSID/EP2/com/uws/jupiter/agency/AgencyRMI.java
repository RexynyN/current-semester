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
import com.uws.jupiter.common.apollo.CodeResult;
import com.uws.jupiter.nameserver.LookupServer;

// Implementação de uma agência usando RMI
public class AgencyRMI extends UnicastRemoteObject implements Agency {
    private List<Agent> agents = new ArrayList<Agent>(); 
    private final Host home;

    public AgencyRMI(Host home) throws RemoteException {
        super();
        this.home = home;
    }

    // Recebe um agente de outra agência e o roda
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

    // Função que roda quando o agente volta pra casa
    @Override
    public void returnHome(String agentName, Agent agent, byte[] byteCodes) throws RemoteException {
        String [] name = agent.getName().split(".");
        System.out.println("Um " + name[name.length - 1] + " acabou de chegar!");
        agents.add(agent);
        // agent.onReturn();
    }

    // Repassa uma mensagem para um agente dessa agência
    @Override
    public void forwardMessage(String agentName, Message message) throws RemoteException {
        System.out.println("Mensagem Recebida!");
        for(Agent suspect : agents){
            if(suspect.getId().equals(agentName))
                suspect.readMessage(message);
        }
    }

    // Cria um seeker e o retorna
    @Override
    public Seeker createSeeker() throws RemoteException {
        String id = "seeker-" + home.getName() + "-" + Utils.getRandomID();
        Seeker newSeeker = new Seeker(home, id);
        agents.add(newSeeker);
        return newSeeker;
    }

    // Cria um worker e o retorna
    @Override
    public Worker createWorker() throws RemoteException {
        String id = "worker-" + home.getName() + "-" + Utils.getRandomID();
        Worker newWorker = new Worker(home, id);
        agentRegister(newWorker);
        agents.add(newWorker);
        return newWorker;
    }

    // Coloca os parâmetros recebidos dentro um worker
    public void setWorkerParams(String id, String payload, String[] argsu){
        Worker sus = null;
        for (int i = 0; i < agents.size(); i++){
            if(agents.get(i).getId().equals(id)){
                sus = (Worker) agents.get(i);
                agents.remove(agents.get(i));
            }
        }

        sus.setPayload(payload);
        sus.setArgs(argsu);
        agents.add(sus);
    }

    // Passa o agente por toda sua "jornada" de ir até uma agência, rodar e voltar
    @Override
    public CodeResult agentJourney(String id, String[] reqs){
        Worker sus = null;
        for (int i = 0; i < agents.size(); i++){
            if(agents.get(i).getId().equals(id)){
                sus = (Worker) agents.get(i);
                agents.remove(agents.get(i));
            }
        }

        System.out.println(sus);

        sus.requestMachine(reqs);
        return sus.getCodeResult();
    }

    // Registra um agente no servidor de nomes
    private void agentRegister(Agent agent) {
        try {
            LookupServer ns = Utils.connectNameServer();
            ns.registerAgent(agent);
        } catch (Exception e) {
            Utils.failPrint("Não foi possível registrar o agente.");
        }
    }
}

