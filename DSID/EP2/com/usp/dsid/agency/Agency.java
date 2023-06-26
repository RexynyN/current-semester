package com.usp.dsid.agency;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.usp.dsid.agency.apollo.ApolloThread;
import com.usp.dsid.agency.apollo.CodeResult;

public class Agency extends UnicastRemoteObject {

    public Agency() throws RemoteException {
        super();
        // TODO Auto-generated constructor stub
    }

    public void runPayload() throws RemoteException {
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

}
