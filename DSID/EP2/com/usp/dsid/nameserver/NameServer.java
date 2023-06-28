package com.usp.dsid.nameserver;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.Scanner;

public class NameServer {
    public static void main(String [] args){
        Scanner scan = new Scanner(System.in);
        String name = "nameserver";
        System.out.println("Servidor de Nomes");
        System.out.print("Porta do servidor: ");
        int port = Integer.parseInt(scan.nextLine());

        try {
            // TODO: Criar uma chamada para o servidor de nomes para registrar o servidor por lá.
            LookupServer server = new LookupServer();
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind(name, server);
            System.out.println("Servidor " + name + " iniciado com sucesso");
        } catch (Exception ex) {
            System.out.println("Erro ao iniciar o servidor: " + ex.getMessage());
            System.exit(0);
        }

        scan.close();
    }
}
