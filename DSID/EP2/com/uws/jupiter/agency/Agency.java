package com.uws.jupiter.agency;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.uws.jupiter.common.Message;
import com.uws.jupiter.common.agents.Agent;
import com.uws.jupiter.common.agents.Seeker;
import com.uws.jupiter.common.agents.Worker;
import com.uws.jupiter.common.apollo.CodeResult;

// Interface que descreve uma agência
public interface Agency extends Remote {
    public void runAgent(String agentName, Agent agent, byte[] byteCodes) throws RemoteException;
    public void returnHome(String agentName, Agent agent, byte[] byteCodes) throws RemoteException;
    public void forwardMessage(String agentName, Message message) throws RemoteException;
    public Seeker createSeeker() throws RemoteException;
    public Worker createWorker() throws RemoteException;
    public CodeResult agentJourney(String id, String[] reqs) throws RemoteException;
    public void setWorkerParams(String id, String payload, String[] argsu) throws RemoteException;
}
