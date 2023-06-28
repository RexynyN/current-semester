package bruh;

import java.io.*;
import java.net.*;

/**
 * An server that creates Sandbox objects for Agent
 * objects
 *
 * @version 1.0
 * @author Prof. David Bernstein, James Madison Univeristy
 */
public class SandboxServer {
    public static final int PORT = 9200;
    public static final String EXT = ".class";

    /**
     * The entry point of the application
     *
     * @param args The command-line arguments
     */
    public static void main(String[] args) {
        Agent agent;
        byte[] byteCodes;
        Class agentClass;
        FileOutputStream out;
        ObjectInputStream in;
        Sandbox sandbox;
        ServerSocket ss;
        Socket s;
        String agentName;

        try {
            ss = new ServerSocket(PORT);

            while (true) {
                s = ss.accept();
                in = new ObjectInputStream(s.getInputStream());

                // Read the class name
                agentName = (String) in.readObject();

                // Read the byte codes
                byteCodes = (byte[]) in.readObject();

                // Save the class on the local file system
                // for tracking purposes
                out = new FileOutputStream(agentName + ".class");
                out.write(byteCodes);

                // Read the Agent
                agent = (Agent) in.readObject();

                // Run the Agent in its own Sandbox
                sandbox = new Sandbox(agent);
                sandbox.start();

                in.close();
                s.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
