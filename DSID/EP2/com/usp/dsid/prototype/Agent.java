package com.usp.dsid.prototype;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

//A simple mobile agent
public abstract class Agent implements Runnable, Serializable {
    private byte[] byteCodes;
    private LinkedList<Host> hosts;
    private Host home;

    public static final String EXT = ".class";

    /**
     * Explicit Value Constructor
     * @param home The "home" host
     */
    public Agent(Host home) {
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
    }

    /**
     * Add a host to the top of the stack
     *
     * @param host The host
     */
    public void addHost(Host host) {
        hosts.addFirst(host);
    }

    /**
     * The code that should be executed before this
     * Agent leaves home
     */
    public abstract void beforeDeparture();

    /**
     * Get the Java byte codes for this Agent
     *
     * @return The byte codes
     */
    public byte[] getByteCodes() {
        return byteCodes;
    }

    /**
     * Get the name of the class for this Agent
     *
     * @return The name
     */
    public String getName() {
        return getClass().getName();
    }

    /**
     * Move this Agent to a Sandbox
     *
     * @param host The host to go to
     */
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
        } 
    }

    /**
     * The code that should be executed when this
     * Agent arrives at a remote Sandbox
     */
    public abstract void onArrival();

    /**
     * The code that should be executed when this
     * Agent returns home
     */
    public abstract void onReturn();

    /**
     * The entry point for the controlling thread
     * of execution
     */
    public void run() {
        Host host;
        if (hosts == null) {
            hosts = new LinkedList<Host>();
            hosts.addLast(home);
            beforeDeparture();
        } 
        else if (hosts.size() == 0) {
            onReturn();
        } 
        else {
            onArrival();

            host = hosts.getFirst();
            hosts.removeFirst();
            goTo(host);
        }
    }

}