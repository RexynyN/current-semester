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
    private static Agency mail;


    private static Scanner input = new Scanner(System.in);
    public  static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public  static final String ANSI_GREEN = "\u001B[32m";

    public static void main(String[] args) {
            
        try {
            
         startAgencys();
         menu();
         controller();
        } catch (Exception e) {
            System.out.println("Error in main: "+e);
        }
        
    }
    static void startAgencys(){
        try{
           
            String infoAgencys [][] = {{"Joji","4040"},{"MotherShip","9200"},{"Stone","3030"}};
           
            
            for(String[] i: infoAgencys){
                String name = i[0];
                int value = Integer.parseInt(i[1]);
                
                self = new Host(value,name);

                initiateServer(self);



            }

        }catch(Exception ex) {
            System.out.println("Erro no metodo start: " + ex.getMessage());
            System.exit(0);
        }
        
    }
    public static void initiateServer(Host home){
        try {
            Agency agency = new AgencyRMI(home);
            Registry registry = LocateRegistry.createRegistry(home.getPort());
            registry.bind(home.getName(), agency);

            registerAgency();
            currentAgency = agency;
        
            Utils.infoPrint("A agência " + home.getName() + " está ativa e registrada!");
            
        } catch (Exception ex) {
            System.out.println("Erro ao iniciar a agência: " + ex.getMessage());
            System.exit(0);
        }


    }

    static void createAgent(){
        
        //Agency agency = LookupServerRMI.searchAgent;
        //Para que o usuario tenha poder de escolher em qual agencia o agente
        //sera criado é preciso:
        // usar esse metodo pra pegar a agencia LookupServerRMI.searchAgent(Agent target)
        // Pega a agência de origem do agente e manda mensagem

        
        try {

            worker = currentAgency.createWorker();
        } catch (RemoteException e) {
            e.printStackTrace();
        } 
        
        System.out.println("Digite a rota do .JAVA: ");
        String rota = input.next();
        worker.setPayloadByPath(rota);
       // Copiar e colar: C:\\Users\\Admin\\codes\\current-semester\\DSID\\EP2\\Blooie.java

        System.out.println("Digite os argumentos: *end para finalizar");
        String args[] = new String[100];

        for(int i = 0; i< 100 ;i++){
            args[i] = input.next();
            if(args[i].equals("end")) break;
        }
        
        worker.setArgs(args);

    }

    static void requesthost(){
        System.out.println("* Digite os requisitos Buscados");
        System.out.println("* Processador:");
        System.out.println("* Memoria HD:");
        System.out.println("* Placa de Video:");
        System.out.println("* Memoria Ram:");

        String argsu[] = new String[4];
        for(int i = 0; i < 4 ;i++)
        argsu[i] = input.next();
        //copiar e colar: i9 64gb integrated 1gb

        worker.requestMachine(argsu);


    }

    static void sendAgent(){
        worker.sendAgent();
    }

    static CodeResult finishAgent(){
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

    static void context(){

        
        System.out.println(ANSI_GREEN + "AGENCIA ATUAL: ");
        System.out.println(ANSI_GREEN + "AGENTE ATUAL: "+worker.getName());

    }
    

    static void bind(){
        AgentLookup agent;
        String name = input.next();
        try{
        LookupServer ns =  Utils.connectNameServer();
        agent = ns.searchAgent(name); 

             Host location = agent.getOriginAgency();

            Registry registry = LocateRegistry.getRegistry("localhost", location.getPort());
             mail = (Agency) registry.lookup(location.getName());
        }catch(Exception e){
            Utils.failPrint("Error in bind: "+e);

        }
    }

    static void unbind(){
        mail = null;
    }

    static void controller(){
        
        String comand ="";

        while(!comand.equals("quit")){
            comand = input.nextLine();


            switch (comand) {
                case "bind":
                bind();

                case "unbind":
                unbind();

                case "context":
                context();
                case "createagent":
                createAgent();

                case "requesthost":
                requesthost();

                case "sendagent":
                sendAgent();

                case "finishagent":
                finishAgent();

                case "comandos":
                menu();

        
            default:
                Utils.failPrint("Digite um comando valido!");
        }
        }


    }

    static void menu(){
        

         System.out.print( ANSI_GREEN +
"********************************* Menu ****************************************************\n"+
"* bind:  Conecta a uma agencia cliente                                                   \n"+
"* unbind:  Desconecta a agencia                                                          \n"+
"* quit: Sai do client                                                                    \n"+
"* context: Mostra quais sao as variaveis no contexto atual                               \n"+
"* createagent: <nome agente>  Cria um agente e coloca como o atual no contexto           \n"+
"* requesthost:   Vai ate o servidor de nomes, chama um seeker, passa as especificacoes   \n"+
"* e espera ele retornar com um host e adiciona ao contexto ou que nao achou uma maquina  \n"+
"* sendagent: Adiciona o host encontrado ao agente atual, manda para o host e espera ele  \n"+
"* voltar com os resultados                                                               \n"+
"* finishagent: Retorna os valores calculados pelo agente e deleta o agente atual         \n"+
"* comandos: exibe comandos disponiveis                                                   \n"+
"******************************************************************************************\n"
                           ); 





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
