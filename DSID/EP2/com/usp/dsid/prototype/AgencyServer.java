package com.usp.dsid.prototype;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class AgencyServer {

    public static void main(String[] args) {
        // Preguiça de digitar isso nos testes omegalul
        String name = "YWing";
        int port = 4444;

        // Scanner scan = new Scanner(System.in);
        // System.out.print("Nome da Agência: ");
        // String name = scan.nextLine().trim();
        // System.out.print("Porta em que a agência deve rodar: ");
        // int port = scan.nextInt();

        try {
            // TODO: Criar uma chamada para o servidor de nomes para registrar o servidor
            // por lá.
            Agency agency = new AgencyRMI();
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind(name, agency);
            System.out.println("A agência " + name + " está ativa!");
        } catch (Exception ex) {
            System.out.println("Erro ao iniciar a agência: " + ex.getMessage());
            System.exit(0);
        }

        // scan.close();
    }
}
