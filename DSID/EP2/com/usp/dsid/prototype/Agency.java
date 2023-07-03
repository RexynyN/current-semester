package com.usp.dsid.prototype;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Agency extends Remote {
    public void runAgent(String agentName, Agent agent, byte[] byteCodes) throws RemoteException;
    public void returnHome(String agentName, Agent agent, byte[] byteCodes) throws RemoteException;
    public void forwardMessage(String agentName, Message message) throws RemoteException;
    public void sendMessage(String agentName, Message message) throws RemoteException;
    public void createSeeker() throws RemoteException;
}
