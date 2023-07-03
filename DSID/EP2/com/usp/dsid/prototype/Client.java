package com.usp.dsid.prototype;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public static void main(String[] args) {
        // Cria o host dessa agência
        Host home = new Host(9200, "MotherShip");
        // Coloca o caminho da classe "Blooie na raiz do projeto, pls"
        Worker worker = new Worker(home, "poggers", "C:\\Users\\Admin\\codes\\current-semester\\DSID\\EP2\\Blooie.java", null);
        
        // Aqui ele abre uma agência pra só pra retornar o worker como teste, mas na implementação final
        // ficaria igual ao "AgencyServer.java" 
        try {
            // TODO: Criar uma chamada para o servidor de nomes para registrar o servidor por lá.
            Agency agency = new AgencyRMI();
            Registry registry = LocateRegistry.createRegistry(home.getPort());
            registry.bind(home.getName(), agency);
            System.out.println("A agência " + home.getName() + " está ativa!");
        } catch (Exception ex) {
            System.out.println("Erro ao iniciar a agência: " + ex.getMessage());
            System.exit(0);
        }

        // Inicia o agente
        worker.run();
        // Manda ele pra uma agência
        worker.goTo(new Host(4444, "Breno"));
    }
}