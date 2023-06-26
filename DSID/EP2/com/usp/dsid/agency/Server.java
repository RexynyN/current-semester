package com.usp.dsid.agency;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Server  {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Entre com o nome do servidor: ");
            String name = sc.nextLine();
            System.out.print("Entre com a porta do servidor: ");
            int port = Integer.parseInt(sc.nextLine());

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
