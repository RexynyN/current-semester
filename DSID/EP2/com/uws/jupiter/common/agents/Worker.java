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
        targetMachine = null;
    }

    public Worker(Host home, String id) {
        super(home, id);
        self = new AgentLookup(id, this.getClassName(), this.home);
    }

    @Override
    public void beforeDeparture() {
        System.out.println(getId() + " saindo em uma aventura!");
        currentHost = targetMachine;
        goTo(currentHost);
    }

    @Override
    public void onArrival(Host host) {
        System.out.println("Olá " + host.getName() + "! " + self.getName() + " rodando aqui!");
        result = Apollo.runCode(payload, args);
    }

    @Override
    public void onReturn() {
        if (result.hadErrors()) {
            Utils.failPrint("O código foi rodado, mas houveram erros!");
        } else {
            Utils.okPrint("O código foi rodado com sucesso!");
        }
    }

    @Override
    public void readMessage(Message msg) {
        switch(msg.getMatter()){
            case "found": {
                Utils.okPrint("Máquina com os requerimentos pedidos foi achada! => " + msg.getBody()[0]);
                targetMachine = findMachineById(msg.getBody()[0]);
                break;
            }

            case "notfound": {
                Utils.failPrint("Não achamos uma máquina para rodar seu código! Mude os requerimentos.");
                break;
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

    public void sendAgent() {
        if(targetMachine != null){
            run();
        } else {
            Utils.failPrint("O agente não tem um host para rodar, procure um host antes!");
        }
    }

    public void requestMachine (String [] requirements){
        try {
            System.out.println("Mandando mensagem pra um seeker");
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

    public void setPayloadByPath (String path){
        this.payload = Utils.readFile(path);
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public CodeResult getCodeResult() {
        return result;
    }
}
