package com.usp.dsid.agency;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.usp.dsid.common.Message;
import com.usp.dsid.common.agents.Agent;

public interface Agency extends Remote {
    public void runAgent(String agentName, Agent agent, byte[] byteCodes) throws RemoteException;
    public void returnHome(String agentName, Agent agent, byte[] byteCodes) throws RemoteException;
    public void forwardMessage(String agentName, Message message) throws RemoteException;
    public void sendMessage(String agentName, Message message) throws RemoteException;
    public void createSeeker() throws RemoteException;
}
