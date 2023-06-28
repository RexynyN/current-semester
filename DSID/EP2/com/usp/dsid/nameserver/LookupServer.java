package com.usp.dsid.nameserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import com.usp.dsid.common.agents.Agent;

public class LookupServer extends UnicastRemoteObject {    
    private List<AgencyLookup> agencies = new ArrayList<AgencyLookup>();
    private List<AgentLookup> agents = new ArrayList<AgentLookup>();

    public LookupServer () throws RemoteException {
        super();
    }

    public void registerAgent(Agent agent, String agency){
        AgentLookup newLookup = new AgentLookup(agent.getName(), agency);
        if(agents.contains(newLookup)){
            return;
        }

        

        agents.add(newLookup);
    }

    public void registerAgency(){

    }

    public String [] listAgents(){
        return new String[] { "" };
    }

    public String seekAgent (){
        return new String ( "" );
    }

    public String [] listAgencies(){
        return new String[] { "" };
    }

    public String seekAgency(){
        return new String ("");
    }
}
