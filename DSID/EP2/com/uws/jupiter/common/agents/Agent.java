package com.uws.jupiter.common.agents;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

import com.uws.jupiter.agency.Agency;
import com.uws.jupiter.common.Host;
import com.uws.jupiter.common.Message;
import com.uws.jupiter.common.Utils;
import com.uws.jupiter.nameserver.AgentLookup;
import com.uws.jupiter.nameserver.LookupServer;

/** A simple mobile agent */
public abstract class Agent implements Runnable, Serializable {
    protected byte[] byteCodes;
    protected LinkedList<Host> hosts;
    protected Host currentHost;
    protected Host home;
    protected String id; 

    public static final String EXT = ".class";

    public Agent(Host home, String id) {
        byteCodes = null;
        try {
            FileInputStream in = new FileInputStream(getClassName() + EXT);
            byteCodes = new byte[in.available()];
            in.read(byteCodes);
            in.close();
        } catch (IOException ioe) {
            byteCodes = null;
        }

        this.home = home;
        this.id = id;
    }

    /** Get the name of this Agent */
    public String getName() {
        return this.id;
    }

    public String getClassName(){
        String[] strips = getClass().getName().split("\\.");
        return strips[strips.length - 1];
    }

    public Host getHome() {
        return home;
    }

    public void goTo(Host host) {
        // Atualiza no servidor de nomes
        updateAgentLocation(host);

        if(host.equals(home)){
            getMeHome();
            return;
        }

        try {
            Registry registry = LocateRegistry.getRegistry(host.getHost(), host.getPort());
            Agency agency = (Agency) registry.lookup(host.getName());
            System.out.println("Conectado com a agência: " + host.getName());

            agency.runAgent(getName(), this, getByteCodes());
        } catch (RemoteException exc) {
            System.out.println("Piu");
            host = hosts.getFirst();
            hosts.removeFirst();
            if (host != null)
                goTo(host);
        } catch(NotBoundException nbe){
            System.out.println("It was not bound");
            nbe.printStackTrace();
        }
    }

    protected void getMeHome(){
        try {
            Registry registry = LocateRegistry.getRegistry(home.getHost(), home.getPort());
            Agency agency = (Agency) registry.lookup(home.getName());
            System.out.println("Retornando um agente para agência: " + home.getName());

            agency.runAgent(getName(), this, getByteCodes());
            hosts.clear();
        } catch (Exception exc) {
            System.out.println("O agente não conseguiu retornar para a agência " + home.getName());
            exc.printStackTrace();
        } 
    }

    public void leaveOnlyHome(){
        hosts.clear();
        hosts.addLast(home);
    }

    /** The entry point for the controlling thread of execution */
    public void run() {
        if (hosts == null) {
            hosts = new LinkedList<Host>();
            hosts.addLast(home);
            beforeDeparture();
        } 
        else if (hosts.size() == 0) {
            onReturn();
            hosts = null;
        } 
        else {
            onArrival(currentHost);
            currentHost = hosts.getFirst();
            hosts.removeFirst();
            goTo(currentHost);
        }
    }

    private void updateAgentLocation(Host newLocation) {
        try {
            LookupServer ns = Utils.connectNameServer();
            ns.updateAgentLocation(this, newLocation);
        } catch (Exception e) {
            Utils.failPrint("Não foi possível dar update na localização do agente.");
            e.printStackTrace();
        }
    }

    public void sendMessage(AgentLookup receiver, Message msg) {
        AgentLookup agent;
        // Pega a agência de origem do agente e manda mensagem
        try {
            LookupServer ns =  Utils.connectNameServer();
            agent = ns.searchAgent(receiver.getName());

            if(agent == null){
                Utils.failPrint("Não foi possível mandar mensagem para o Agente");
                return;
            }
            
            Host location = agent.getOriginAgency();
            Registry registry = LocateRegistry.getRegistry("localhost", location.getPort());
            Agency mail = (Agency) registry.lookup(location.getName());

            mail.forwardMessage(agent.getName(), msg);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    /** Get the Java byte codes for this Agent */
    public byte[] getByteCodes() {
        return byteCodes;
    }

    public String getId() {
        return id;
    }

    /** Add a host to the top of the stack */
    public void addHost(Host host) {
        hosts.addFirst(host);
    }

    /** The code that should be executed before this Agent leaves home */
    public abstract void beforeDeparture();

    /** The code that should be executed when this Agent arrives at a remote Sandbox */
    public abstract void onArrival(Host host);

    /** The code that should be executed when this Agent returns home */
    public abstract void onReturn();

    // Decide o que fazer com a mensagem recebida de algum agente
    public abstract void readMessage(Message msg);
}