package com.uws.jupiter.nameserver;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.uws.jupiter.agency.Agency;
import com.uws.jupiter.agency.AgencyRMI;
import com.uws.jupiter.common.Host;
import com.uws.jupiter.common.Utils;

// Servidor de nomes
public class NameServer {
    private static final String name = "NameServer";
    private static final int port = 5454;
    private static final String seekerName = "SeekerServer";
    private static final int seekerPort = 4545;

    // Inicializa o servidor de nomes e o servidor de seekers único
    public static void main(String [] args){
        initiateNameServer();
        initiateSeekerServer();
    }

    // Inicializa o servidor de nomes
    public static void initiateNameServer(){
        try {
            LookupServer server = new LookupServerRMI();
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind(name, server);
            System.out.println("Servidor de Nomes iniciado com sucesso");
        } catch (Exception ex) {
            System.out.println("Erro ao iniciar o servidor: " + ex.getMessage());
            System.exit(0);
        }
    }

    // Inicia o servidor de seekers
    public static void initiateSeekerServer(){
        Host seekerHost = new Host(seekerPort, seekerName);
        try {
            Agency server = new AgencyRMI(seekerHost);
            Registry registry = LocateRegistry.createRegistry(seekerPort);
            registry.bind(seekerName, server);
            registerSeekerServer(seekerHost);
            System.out.println("Servidor de Seekers iniciado com sucesso");
        } catch (Exception ex) {
            System.out.println("Erro ao iniciar o servidor de Seekers: " + ex.getMessage());
            System.exit(0);
        }
    }

    // Registra o servidor de seekers
    private static void registerSeekerServer(Host ss) {
        try {
            LookupServer nameServer = Utils.connectNameServer();
            nameServer.registerSeekerAgency(ss);
        } catch (RemoteException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
