package bruh;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * A simple mobile agent
 *
 * @version 1.0
 * @author Prof. David Bernstein, James Madison University
 */
public abstract class Agent implements Runnable, Serializable {
    private byte[] byteCodes;
    private LinkedList<String> hosts;
    private String home;

    public static final int PORT = 9200;
    public static final String EXT = ".class";

    /**
     * Explicit Value Constructor
     *
     * @param home The "home" host
     */
    public Agent(String home) {
        FileInputStream in;

        byteCodes = null;

        try {
            in = new FileInputStream(getName() + EXT);
            byteCodes = new byte[in.available()];
            in.read(byteCodes);
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
    public void addHost(String host) {
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
    public void goTo(String host) {
        ObjectOutputStream out;
        Socket s;

        try {
            s = new Socket(host, PORT);
            out = new ObjectOutputStream(s.getOutputStream());

            out.writeObject(getName());
            out.writeObject(getByteCodes());
            out.writeObject(this);
        } catch (IOException ioe) {
            host = hosts.getFirst();
            hosts.removeFirst();
            if (host != null)
                goTo(host);
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
        String host;

        if (hosts == null) {
            hosts = new LinkedList<String>();
            hosts.addLast(home);
            beforeDeparture();
        } else if (hosts.size() == 0) {
            onReturn();
        } else {
            onArrival();

            host = hosts.getFirst();
            hosts.removeFirst();
            goTo(host);
        }

        // Drop out of the run method which
        // will terminate the thread on the old
        // Sandbox
    }

}