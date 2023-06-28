package com.usp.dsid.common;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.usp.dsid.common.apollo.ApolloThread;
import com.usp.dsid.common.apollo.CodeResult;
import com.usp.dsid.common.agents.Agent;
import com.usp.dsid.common.agents.Worker;

public class Agency extends UnicastRemoteObject implements Runnable {
    private Queue<Agent> tasks = new LinkedList<Agent>();
    private List<Agent> finishedAgents = new ArrayList<Agent>();

    public Agency() throws RemoteException {
        super();
    }

    public void receiveAgent(Agent newAgent){
        
    }

    public boolean checkAgent(int id){
        return true;
    }

    public Agent returnAgent(int id){
        return new Worker("");
    }

    public void runPayload() {
        String[] argus = new String[] { "2", "2" };
        String program = "public class TestClass {\n"
                + "    public static int main (String [] args) {\n"
                + "        System.out.println(\"Hello Everynyan!\");\n"
                + "        int a = Integer.parseInt(args[0]);\n"
                + "        int b = Integer.parseInt(args[1]);\n"
                + "        a = a + b;\n"
                + "        System.out.println(a);\n"
                + "        return a;\n"
                + "    }\n"
                + "}\n";

        try {
            ApolloThread at = new ApolloThread(program, argus);
            Thread t = new Thread(at);
            t.start();
            t.join();
            CodeResult output = at.getResult();
            // CodeResult output = Apollo.runCode(program, argus);
            if (output.hadErrors()) {
                String compilationErrors = output.getCompilationErrors();
                System.out.println(compilationErrors);
            } else {
                System.out.println(output.getLogs());
                System.out.println(output.getReturnValue());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

}
