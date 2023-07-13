package com.uws.jupiter.common.agents;

import java.util.LinkedList;

import com.uws.jupiter.common.Host;
import com.uws.jupiter.common.Message;
import com.uws.jupiter.common.Utils;
import com.uws.jupiter.common.apollo.Apollo;
import com.uws.jupiter.common.apollo.CodeResult;
import com.uws.jupiter.nameserver.AgentLookup;
import com.uws.jupiter.nameserver.LookupServer;

public class Worker extends Agent {
    private String payload;
    private String[] args;
    private CodeResult result;
    private AgentLookup self;
    private Host targetMachine;

    /**
     * Um agente que carrega código para ser executado
    */
    public Worker(Host home, String id, String codePath, String[] args) {
        super(home, id);
        this.payload = Utils.readFile(codePath);
        this.args = args;
        self = new AgentLookup(id, this.getClassName(), this.home);
    }

    public Worker(Host home, String id) {
        super(home, id);
        self = new AgentLookup(id, this.getClassName(), this.home);
    }

    @Override
    public void beforeDeparture() {
        System.out.println(getId() + " saindo em uma aventura!");
    }

    @Override
    public void onArrival(Host host) {
        System.out.println("Olá " + host.getName() + "! Estou rodando aqui!");
        result = Apollo.runCode(payload, args);
    }

    @Override
    public void onReturn() {
        // CodeResult result = Apollo.runCode(program, argus);
        if (result.hadErrors()) {
            String compilationErrors = result.getCompilationErrors();
            System.out.println(compilationErrors);
        } else {
            System.out.println(result.getLogs());
            System.out.println(result.getReturnValue());
        }
    }

    @Override
    public void readMessage(Message msg) {
        switch(msg.getMatter()){
            case "found": {
                targetMachine = findMachineById(msg.getBody()[0]);
                hosts = new LinkedList<Host>();
                hosts.addLast(home);
                hosts.addLast(targetMachine);
                run();
            }
            case "notfound": {
                Utils.failPrint("Não achamos uma máquina para rodar seu máquina!");
            }
        }
    }

    private Host findMachineById(String agencyName) {
        try {
            LookupServer ls = Utils.connectNameServer();
            return ls.searchAgency(agencyName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void requestMachine (String [] requirements){
        try {
            LookupServer ns =  Utils.connectNameServer();
            AgentLookup seeker = ns.requestSeeker();

            Message msg = new Message(self, "seek", requirements);
            sendMessage(seeker, msg); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }
}
