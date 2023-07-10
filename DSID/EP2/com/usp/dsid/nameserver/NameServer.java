package com.usp.dsid.nameserver;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class NameServer {
    public static void main(String [] args){
        String name = "nameserver";
        int port = 5454;

        System.out.println("Servidor de Nomes");

        try {
            LookupServer server = new LookupServer();
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind(name, server);
            System.out.println("Servidor de Nomes iniciado com sucesso");
        } catch (Exception ex) {
            System.out.println("Erro ao iniciar o servidor: " + ex.getMessage());
            System.exit(0);
        }
    }
}
