package bruh;

import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SandboxRMI extends UnicastRemoteObject {
    public SandboxRMI(int port) throws RemoteException {
        super(port);
    }

    public void runWorker(String agentName, Agent agent, byte[] byteCodes) {
        try {
            // Save the class on the local file system
            FileOutputStream out = new FileOutputStream(agentName + ".class");
            out.write(byteCodes);

            // Run the Agent in its own Sandbox
            Sandbox sandbox = new Sandbox(agent);
            sandbox.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}

