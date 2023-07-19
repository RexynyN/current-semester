package com.uws.jupiter.agency;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import com.uws.jupiter.common.Host;
import com.uws.jupiter.common.Utils;
import com.uws.jupiter.nameserver.LookupServer;

// Cria uma agência sem um client, ou seja, que é só server para receber agentes
public class AgencyServer {
    private static Host self;

    public static void main(String[] args) {
        // Pega as informações para iniciar um agência
        Scanner scan = new Scanner(System.in);
        System.out.print("Nome da Agência: ");
        String name = scan.nextLine().trim();
        System.out.print("Porta em que a agência deve rodar: ");
        int port = scan.nextInt();
        scan.close();

        self = new Host(port, name);
        initiateServer(self);
    }

    // Inicializa uma agência de fato
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

    // Registra uma agência no servidor de nomes
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
