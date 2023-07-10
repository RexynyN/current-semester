package com.usp.dsid.agency;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import com.usp.dsid.common.Utils;

public class AgencyServer {
    public static void main(String[] args) {
        // Checa se tem alguma coisa nos argumentos
        if (args.length == 0){
            // Preguiça de digitar isso nos testes omegalul
            String name = "YWing";
            int port = 4444;

            // Scanner scan = new Scanner(System.in);
            // System.out.print("Nome da Agência: ");
            // String name = scan.nextLine().trim();
            // System.out.print("Porta em que a agência deve rodar: ");
            // int port = scan.nextInt();

            initiateServer(name, port);
            return;
        }

        if(args.length < 3){
            Utils.failPrint("Erro ao iniciar o servidor de nomes, argumentos inválidos!");
            Utils.infoPrint("Argumentos: <tipo servidor (seeker | worker)> <nome> <porta>");
        }

        // TODO: Fazer a lógica do servidor de seekers
    }

    public static void initiateServer(String name, int port){
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
