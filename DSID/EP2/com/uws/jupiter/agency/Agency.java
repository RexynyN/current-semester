package com.uws.jupiter.agency;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.uws.jupiter.common.Message;
import com.uws.jupiter.common.agents.Agent;

public interface Agency extends Remote {
    public void runAgent(String agentName, Agent agent, byte[] byteCodes) throws RemoteException;
    public void returnHome(String agentName, Agent agent, byte[] byteCodes) throws RemoteException;
    public void forwardMessage(String agentName, Message message) throws RemoteException;
    public Agent createAgent(String type) throws RemoteException;
}
