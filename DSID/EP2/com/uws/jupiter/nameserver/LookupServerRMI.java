package com.uws.jupiter.nameserver;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import com.uws.jupiter.agency.Agency;
import com.uws.jupiter.common.AgentLookup;
import com.uws.jupiter.common.Host;
import com.uws.jupiter.common.Utils;
import com.uws.jupiter.common.agents.Agent;
import com.uws.jupiter.common.agents.Seeker;

public class LookupServerRMI extends UnicastRemoteObject implements LookupServer{    
    private List<Host> agencies = new ArrayList<Host>();
    private List<AgentLookup> agents = new ArrayList<AgentLookup>();
    private Agency seekerAgency;

    public LookupServerRMI () throws RemoteException {
        super();
    }

    // Adiciona um novo agente na lista de agentes
    public boolean registerAgent(Agent agent){
        AgentLookup newLookup = new AgentLookup(agent.getName(), agent.getClassName(), agent.getHome());
        if(agents.contains(newLookup)){
            return false;
        }

        agents.add(newLookup);
        Utils.okPrint("Novo agente registrado! => " + agent.getName());
        return true;
    }

    // Atualiza a localização atual do agente
    public boolean updateAgentLocation(Agent agent, Host newAgency){
        AgentLookup oldLookup = new AgentLookup(agent.getName(), agent.getClassName(), agent.getHome());
        if(!agents.contains(oldLookup)){
            return false;
        }
        
        int index = agents.indexOf(oldLookup);
        AgentLookup newLookup = new AgentLookup(agent.getName(), agent.getClassName(), agent.getHome());
        newLookup.setCurrentAgency(newAgency);
        agents.set(index, newLookup);
        return true;
    }

    // Apaga um agente da lista
    public boolean killAgent(Agent agent){
        return agents.remove(new AgentLookup(agent.getName(), agent.getClassName(), agent.getHome()));
    }

    // Procura um agente pelo ID
    public AgentLookup searchAgent(String id){
        for (AgentLookup agent : agents){
            if(agent.getName().equals(id))
                return agent;
        }

        return null;
    }

    // Procura um lookup de agente usando o próprio agente
    public AgentLookup searchAgent(Agent target){
        AgentLookup targetLookup = new AgentLookup(target.getName(), target.getClassName(), target.getHome());
        for (AgentLookup agent : agents){
            if(agent.equals(targetLookup))
                return agent;
        }

        return null;
    }

    // Adiciona uma nova agência na lista de agências 
    public boolean registerAgency(Host agency){
        if(agencies.contains(agency)){
            return false;
        }

        agencies.add(agency);
        Utils.okPrint("Nova agência registrada! => " + agency.getName());
        return true;
    }

    // Destroí uma agência
    public boolean killAgency(Host agent){
        Utils.infoPrint("Agente deletado! => " + agent.getName());
        return agencies.remove(agent);
    }

    // Procura uma agência por ID
    public Host searchAgency(String id){
        for (Host agent : agencies){
            if(agent.getName().equals(id))
                return agent;
        }

        return null;
    }

    // Faz a ponta entre uma agência e agência de seekers retornar um seeker
    public AgentLookup requestSeeker() throws RemoteException {
        Seeker newSeeker = seekerAgency.createSeeker();

        AgentLookup sal = new AgentLookup(newSeeker.getName(), newSeeker.getClassName(), newSeeker.getHome());
        this.registerAgent(newSeeker);

        return sal;
    }

    // Registra uma agência de seekers
    public void registerSeekerAgency(Host newSeeker){  
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", newSeeker.getPort());
            Agency lookup = (Agency) registry.lookup(newSeeker.getName());
            this.seekerAgency = lookup;
            Utils.okPrint("Servidor de Seekers foi registrado!");
        } catch (Exception e) {
            Utils.failPrint("Servidor de Seekers não foi registrado.");
            System.exit(1);
        }

    }

    // Lista todos os agentes
    public List<AgentLookup> listAgents() throws RemoteException {
        return agents;
    }

    // Lista todas as agências
    public List<Host> listAgencies() throws RemoteException {
        return agencies;
    }
}
