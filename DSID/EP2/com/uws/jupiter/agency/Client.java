package com.uws.jupiter.agency;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.uws.jupiter.common.Host;
import com.uws.jupiter.common.Utils;
import com.uws.jupiter.common.agents.Worker;
import com.uws.jupiter.nameserver.LookupServer;

public class Client {
    // Cria o host dessa agência
    private static Host home;
    private static Agency agency;
    private static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        // Pega as informações para iniciar um agência
        System.out.print("Nome da Agência: ");
        String name = scan.nextLine().trim();
        System.out.print("Porta em que a agência deve rodar: ");
        int port = scan.nextInt();

        home = new Host(port, name);
        agency = null;
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

        initiateClient();
    }

    // Faz o loop do cliente
    private static void initiateClient() {
        while (true) {
            // Cria um agente
            Worker worker = null;
            try {
                worker = agency.createWorker();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            Scanner input = new Scanner(System.in);
            // Pega os requisitos para a máquina desejada
            String[] reqs = new String[4];

            Utils.infoPrint("* Digite os requisitos Buscados na máquina");
            Utils.infoPrint("* Processador:");
            reqs[0] = input.nextLine().trim();

            Utils.infoPrint("\n* Memória HDD/SSD:");
            reqs[1] = input.nextLine().trim();

            Utils.infoPrint("\n* Placa de Video:");
            reqs[2] = input.nextLine().trim();

            Utils.infoPrint("\n* Memoria Ram:");
            reqs[3] = input.nextLine().trim();

            // Pega o código .java
            Utils.infoPrint("\n* Selecione o arquivo .java para ser rodado no agente");
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Java Source Code", "java");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                worker.setPayloadByPath(chooser.getSelectedFile().getAbsolutePath());
            } else {
                Utils.failPrint("Você não escolheu um arquivo válido");
                continue;
            }

            // Pega os argumentos
            System.out.println("* Digite os argumentos, separados por espaços.");
            String rawArgs = input.nextLine();

            // Formata os argumentos
            String[] argsu = rawArgs.split(" ");
            worker.setArgs(argsu);

            // Chama um seeker pra iniciar o processo de procurar/rodar uma máquina
            worker.requestMachine(reqs);

            System.out.println("ENTER pra rodar outro agente, 'quit' para sair");
            if(input.nextLine().trim().toLowerCase().equals("quit"))
                break;
        }
    }

    // Registra a agência desse cliente
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