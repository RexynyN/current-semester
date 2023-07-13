package com.uws.jupiter.common.agents;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;

import com.uws.jupiter.common.Host;
import com.uws.jupiter.common.Message;
import com.uws.jupiter.common.Utils;
import com.uws.jupiter.nameserver.AgentLookup;
import com.uws.jupiter.nameserver.LookupServer;

/**
 * Um agente que procura uma agência que esteja rodando numa máquina com as especificações passadas
*/
public class Seeker extends Agent {
    private String [] requirements;
    private AgentLookup self;
    private AgentLookup requester;
    private Message returnMessage;
    
    public Seeker(Host home, String id) {
        super(home, id);
        returnMessage = null;
        self = new AgentLookup(id, this.getClassName(), this.home);
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

        returnMessage = new Message(self, "found", new String[] { host.getName() });
        this.getMeHome();
    }

    @Override
    public void onReturn() {
        // Se ele achar uma máquina, retorna.
        if (returnMessage != null){
            sendMessage(requester, returnMessage);
            returnMessage = null;
            return;
        }
        // Se não, retorna negativo
        sendMessage(requester, new Message(self, "notfound", new String[] { }));
    }
    
    private void seekMachine(String [] body) {
        this.requirements = body;

        // Fazendo os trâmites antes de sair do metodo run
        hosts = new LinkedList<Host>();
        hosts.addLast(home);
        this.beforeDeparture();

        try {
            LookupServer ns =  Utils.connectNameServer();
            List<Host> agencies = ns.listAgencies();
            for(Host host : agencies){
                hosts.addLast(host);
            }

            this.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readMessage(Message msg) {
        switch(msg.getMatter()){
            case "seek": {
                this.requester = msg.getSender();
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
