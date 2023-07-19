package com.uws.jupiter.agency;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.uws.jupiter.common.Host;
import com.uws.jupiter.common.Utils;
import com.uws.jupiter.common.agents.Worker;
import com.uws.jupiter.common.apollo.CodeResult;
import com.uws.jupiter.nameserver.AgentLookup;
import com.uws.jupiter.nameserver.LookupServer;
import com.uws.jupiter.nameserver.LookupServerRMI;

public class Client2 {

    private static Agency currentAgency;
    private static Host self;
    private static Worker worker;

    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        try {

            menu();
            controller();
        } catch (Exception e) {
            System.out.println("Error in main: " + e);
            e.printStackTrace();
        }

    }

    static void startAgencys() {
        try {

            String infoAgencys[][] = { { "Joji", "4040" }, { "MotherShip", "9200" }, { "Stone", "3030" } };

            for (String[] i : infoAgencys) {
                String name = i[0];
                int value = Integer.parseInt(i[1]);

                self = new Host(value, name);

                // para não refazer nessa classe, reaproveitamos o metodo:
                AgencyServer.initiateServer(self);

            }

        } catch (Exception ex) {
            System.out.println("Erro no metodo start: " + ex.getMessage());
            System.exit(0);
        }

    }

    static void createAgent() {

        // Agency agency = LookupServerRMI.searchAgent;
        // Para que o usuario tenha poder de escolher em qual agencia o agente
        // sera criado é preciso:
        // usar esse metodo pra pegar a agencia LookupServerRMI.searchAgent(Agent
        // target)
        // Pega a agência de origem do agente e manda mensagem

        try {

            worker = currentAgency.createWorker();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        System.out.println("* Digite a rota do .JAVA: ");
        String rota = input.nextLine();
        worker.setPayloadByPath(rota);
        // Copiar e colar:
        // C:\\Users\\Admin\\codes\\current-semester\\DSID\\EP2\\Blooie.java

        //C:\\Users\\gabri\\Desktop\\faculdade\\5-semestre\\distribuidos\\nova\\current-semester\\DSID\\EP2\\Blooie.java
        System.out.println("* Digite os argumentos, separados por espaços.");
        String rawArgs = input.nextLine();

        String [] args = rawArgs.split(" ");
        worker.setArgs(args);
    }

    static void requesthost() {
        String argsu[] = new String[4];
        System.out.println("* Digite os requisitos Buscados");
        System.out.println("* Processador:");
        argsu[0] = input.nextLine();

        System.out.println("* Memória HDD/SSD:");
        argsu[1] = input.nextLine();

        System.out.println("* Placa de Video:");
        argsu[2] = input.nextLine();
        System.out.println("Placa de Video:"+argsu[2]);

        System.out.println("* Memoria Ram:");
        argsu[3] = input.nextLine();

        worker.requestMachine(argsu);
    }

    static void sendAgent() {
        worker.sendAgent();
    }

    static CodeResult finishAgent() {
        CodeResult result = worker.getCodeResult();

        // Esse aqui é pra caso ele queira printar na tela
        if (result.hadErrors()) {
            String compilationErrors = result.getCompilationErrors();
            System.out.println(compilationErrors);
        } else {
            System.out.println(result.getLogs());
            System.out.println(result.getReturnValue());
        }

        return result;
    }


       

    

    static void bind() {
        Host location;
        String name = input.nextLine();
        try {
            LookupServer ns = Utils.connectNameServer();
            location = ns.searchAgency(name);

            Registry registry = LocateRegistry.getRegistry("localhost", location.getPort());
            currentAgency = (Agency) registry.lookup(location.getName());
              System.out.println("Ok! Bind()");
        } catch (Exception e) {
            Utils.failPrint("Error in bind: " + e);
            e.printStackTrace();
        }
    }

    static void unbind() {
        currentAgency = null;
    }

    static void controller() {
        String comand = "";
        while (!comand.equals("quit")) {
            comand = input.nextLine();
            System.out.println(comand);

            switch (comand) {
                case "bind": 
                    bind();
                    break;

                case "unbind":
                    unbind();
                    break;

                case "createagent":
                    createAgent();
                    break;

                case "requesthost":
                    requesthost();
                    break;

                case "sendagent":
                    sendAgent();
                    break;

                case "finishagent":
                    finishAgent();
                    break;

                case "comandos":
                    menu();
                    break;

                default:
                    Utils.failPrint("Digite um comando valido!");
                    break;
            }
        }

    }

    static void menu() {

        Utils.okPrint( 
                "********************************* Menu ****************************************************\n"+
                "* bind:  Conecta a uma agencia cliente                                                   \n" +
                "* unbind:  Desconecta a agencia                                                          \n" +
                "* quit: Sai do client                                                                    \n" +
                "* context: Mostra quais sao as variaveis no contexto atual                               \n" +
                "* createagent: <nome agente>  Cria um agente e coloca como o atual no contexto           \n" +
                "* requesthost:   Vai ate o servidor de nomes, chama um seeker, passa as especificacoes   \n" +
                "* e espera ele retornar com um host e adiciona ao contexto ou que nao achou uma maquina  \n" +
                "* sendagent: Adiciona o host encontrado ao agente atual, manda para o host e espera ele  \n" +
                "* voltar com os resultados                                                               \n" +
                "* finishagent: Retorna os valores calculados pelo agente e deleta o agente atual         \n" +
                "* comandos: exibe comandos disponiveis                                                   \n" +
                "******************************************************************************************\n");

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
