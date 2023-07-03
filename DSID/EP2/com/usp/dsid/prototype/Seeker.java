package com.usp.dsid.prototype;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;

import com.usp.dsid.common.Utils;

/**
 * Um agente que procura uma agência que esteja rodando numa máquina com as especificações passadas
*/
public class Seeker extends Agent {
    private String [] requirements;
    private Message requester; 

    public Seeker(Host home, String id) {
        super(home, id);
    }

    @Override
    public void beforeDeparture() {
        System.out.println(getId() + " indo procurar uma máquina bonita!");
    }

    @Override
    public void onArrival(Host host) {
        String [] reqs = Utils.readFile(host.getName() + ".txt").split("\n");

        boolean equal = true;
        for(int i = 0; i < reqs.length; i++){
            if (reqs[i] == requirements[i]){
                equal = false;
                break;
            }
        }

        if(!equal)
            return;

        leaveOnlyHome();
        // Message found = new Message(requester, requester);
    }

    @Override
    public void onReturn() {
        throw new UnsupportedOperationException("Unimplemented method 'onReturn'");
    }
    
    @Override
    public void sendMessage(Host receiver, Message msg) {
        // TODO: Aqui a gente chama o name server, faça isso.
        Host host = new Host(4444, "MotherShip");

        try {
            Registry registry = LocateRegistry.getRegistry(host.getHost(), host.getPort());
            Agency agency = (Agency) registry.lookup(host.getName());
            System.out.println("Retornando um agente para agência: " + host.getName());

            agency.forwardMessage(receiver.getId(), msg);
        } catch (Exception exc) {
            System.out.println("A mensagem para a agência " + host.getName() + " não pode ser enviada.");
            exc.printStackTrace();
        }
    }

    private void seekMachine(String [] body) {
        // TODO: Aqui a gente chama o name server e bota literalmente todos as máquinas próximas lol, faça isso.
        Host host = new Host(4444, "YWing");
        hosts = new LinkedList<Host>();
        hosts.add(host);

        requirements = body;

        this.run();
    }

    @Override
    public void readMessage(Message msg) {
        switch(msg.getMatter()){
            case "seek": {
                requester = msg.getSender();
                seekMachine((String []) msg.getBody());                
            }
        }
    }

    public String[] getRequirements() {
        return requirements;
    }

    public void setRequirements(String[] requirements) {
        this.requirements = requirements;
    }
}
