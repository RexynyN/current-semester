package com.usp.dsid.agency;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import com.usp.dsid.common.Agency;

public class Server implements Runnable {
    private String name;
    private int port;

    public Server (String name, int port){
        this.name = name;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            // TODO: Criar uma chamada para o servidor de nomes para registrar o servidor
            // por lá.
            Agency agency = new Agency();
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind(name, agency);
            System.out.println("Servidor " + name + " iniciado com sucesso");
        } catch (Exception ex) {
            System.out.println("Erro ao iniciar o servidor: " + ex.getMessage());
            System.exit(0);
        }
    }
    

}
