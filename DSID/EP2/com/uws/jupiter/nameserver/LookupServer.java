package com.uws.jupiter.nameserver;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.uws.jupiter.common.Host;
import com.uws.jupiter.common.agents.Agent;

public interface LookupServer extends Remote {
    public List<AgentLookup> listAgents() throws RemoteException;
    public boolean registerAgent(Agent agent) throws RemoteException;
    public boolean updateAgentLocation(Agent agent, Host newAgency) throws RemoteException;
    public boolean killAgent(Agent agent) throws RemoteException;
    public AgentLookup searchAgent(String id) throws RemoteException;
    public AgentLookup searchAgent(Agent target) throws RemoteException;
    public boolean registerAgency(Host agency) throws RemoteException;
    public boolean killAgency(Host agent) throws RemoteException;
    public Host searchAgency(String id) throws RemoteException;
    public List<Host> listAgencies() throws RemoteException;
    public void registerSeekerAgency(Host newSeeker) throws RemoteException;  
    public AgentLookup requestSeeker() throws RemoteException;
}
