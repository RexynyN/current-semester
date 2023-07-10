package com.usp.dsid.common.agents;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

import com.usp.dsid.agency.Agency;
import com.usp.dsid.common.Host;
import com.usp.dsid.common.Message;

/** A simple mobile agent */
public abstract class Agent implements Runnable, Serializable {
    protected byte[] byteCodes;
    protected LinkedList<Host> hosts;
    protected Host home;
    protected String id; 

    public static final String EXT = ".class";

    public Agent(Host home, String id) {
        byteCodes = null;
        try {
            FileInputStream in = new FileInputStream(getName() + EXT);
            byteCodes = new byte[in.available()];
            in.read(byteCodes);
            in.close();
        } catch (IOException ioe) {
            byteCodes = null;
        }

        this.home = home;
        this.id = id;
    }

    /** Get the name of the class for this Agent */
    public String getName() {
        return getClass().getName();
    }

    /** Move this Agent to a Sandbox */
    public void goTo(Host host) {
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
            exc.printStackTrace();
        } catch(NotBoundException nbe){
            System.out.println("It was not bound");
            nbe.printStackTrace();
        }
    }

    private void getMeHome(){
        try {
            Registry registry = LocateRegistry.getRegistry(home.getHost(), home.getPort());
            Agency agency = (Agency) registry.lookup(home.getName());
            System.out.println("Retornando um agente para agência: " + home.getName());

            agency.runAgent(getName(), this, getByteCodes());
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
        Host host;
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
            host = hosts.getFirst();
            hosts.removeFirst();
            onArrival(host);
            goTo(host);
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

    public abstract void readMessage(Message msg);

    public abstract void sendMessage(Host receiver, Message msg);
}