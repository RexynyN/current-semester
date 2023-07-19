package com.uws.jupiter.agency;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import com.uws.jupiter.common.Host;
import com.uws.jupiter.common.Utils;
import com.uws.jupiter.nameserver.LookupServer;

public class AgencyServer {
    private static Host self;

    public static void main(String[] args) {
        // Preguiça de digitar isso nos testes omegalul
        String name = "YWing";
        int port = 4444;

        // Scanner scan = new Scanner(System.in);
        // System.out.print("Nome da Agência: ");
        // String name = scan.nextLine().trim();
        // System.out.print("Porta em que a agência deve rodar: ");
        // int port = scan.nextInt();

        self = new Host(port, name);
        initiateServer(self);
         name = "Joji";
         port = 1010;
        self = new Host(port, name);
        initiateServer(self);
    }

    public static void initiateServer(Host home){
        try {
            Agency agency = new AgencyRMI(home);
            Registry registry = LocateRegistry.createRegistry(home.getPort());
            registry.bind(home.getName(), agency);

            registerAgency();
        
            System.out.println("A agência " + home.getName() + " está ativa e registrada!");
        } catch (Exception ex) {
            System.out.println("Erro ao iniciar a agência: " + ex.getMessage());
            System.exit(0);
        }
    }

    private static void registerAgency() {
        try {
            LookupServer nameServer = Utils.connectNameServer();
            nameServer.registerAgency(self);
        } catch (RemoteException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
