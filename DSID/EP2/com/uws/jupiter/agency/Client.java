package com.uws.jupiter.agency;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.uws.jupiter.common.Host;
import com.uws.jupiter.common.Utils;
import com.uws.jupiter.common.agents.Worker;
import com.uws.jupiter.common.apollo.CodeResult;
import com.uws.jupiter.nameserver.LookupServer;

public class Client {
    // Cria o host dessa agência
    private static final Host home = new Host(9200, "MotherShip");
    public static void main(String[] args) {
        Agency agency = null;
        try {
            agency = new AgencyRMI(home);
            Registry registry = LocateRegistry.createRegistry(home.getPort());
            registry.bind(home.getName(), agency);

            registerAgency();

            System.out.println("A agência " + home.getName() + " está ativa!");
        } catch (Exception ex) {
            System.out.println("Erro ao iniciar a agência: " + ex.getMessage());
            System.exit(0);
        }

        // ====================== DAQUI PRA BAIXO É OQ O CLIENTE TEM QUE FAZER ==============================

        // ************* createagent *****************

        // Cria um agente
        Worker worker = null;
        try {
            worker = agency.createWorker();
        } catch (RemoteException e) {
            e.printStackTrace();
        } 

        // Coloca o código e os argumentos
        worker.setPayloadByPath("C:\\Users\\Admin\\codes\\current-semester\\DSID\\EP2\\Blooie.java");
        worker.setArgs(new String [] { "21", "12" });
        String [] argsu = new String[] { "i7", "32gb", "gtx 1080 ti", "4gb" };

        // ************* requesthost *****************

        // Chama um seeker pra iniciar o processo
        worker.requestMachine(argsu);

        // Não precisa disso no client, é só pra demo funcionar
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        // ************* sendagent *****************

        // Manda o worker pro host achado
        worker.sendAgent();

        // Não precisa disso no client, é só pra demo funcionar
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        // ************* finishagent *****************

        // Retorna tudo oq ele calculou
        CodeResult result = worker.getCodeResult();

        // Esse aqui é pra caso ele queira printar na tela
        if (result.hadErrors()) {
            String compilationErrors = result.getCompilationErrors();
            System.out.println(compilationErrors);
        } else {
            System.out.println(result.getLogs());
            System.out.println(result.getReturnValue());
        }

        // Caso ele queira colocar num arquivo, só colocar cada string em um .txt que tá ótimo
    }


    private static void registerAgency() {
        try {
            LookupServer nameServer = Utils.connectNameServer();
            nameServer.registerAgency(home);
        } catch (RemoteException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}