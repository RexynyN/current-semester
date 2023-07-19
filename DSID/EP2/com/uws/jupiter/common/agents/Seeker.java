package com.uws.jupiter.common.agents;

import java.util.List;

import com.uws.jupiter.common.AgentLookup;
import com.uws.jupiter.common.Host;
import com.uws.jupiter.common.Message;
import com.uws.jupiter.common.Utils;
import com.uws.jupiter.nameserver.LookupServer;

// Um agente que procura uma agência que esteja rodando numa máquina com as especificações passadas
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

    // Antes de sair, ele pega todas as agências disponíveis e coloca com destinos
    @Override
    public void beforeDeparture() {
        System.out.println(getName() + " indo procurar uma máquina bonita!");
        try {
            LookupServer ns =  Utils.connectNameServer();
            List<Host> agencies = ns.listAgencies();
            for(Host host : agencies){
                hosts.addLast(host);
            }

            // Começa o primeiro "laço" de visitar todas as agências
            currentHost = hosts.getFirst();
            hosts.removeFirst();
            goTo(currentHost);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Assim que chega na agência estrangeira, ele lê o <agência>.txt e vê se as especificações são iguais
    @Override
    public void onArrival(Host host) {
        String [] reqs = Utils.readFileAllLines(host.getName() + ".txt");
        boolean equal = true;
        for(int i = 0; i < reqs.length; i++){
            if (!reqs[i].trim().equals(requirements[i].trim())){
                equal = false;
                break;
            }
        }

        if(equal){
            returnMessage = new Message(self, "found", new String[] { host.getName() });
        }
    }

    // Ao retorna para a agência de Seekers, ele manda uma mensagem reportando se achou ou não uma agência
    @Override
    public void onReturn() {
        // Se ele achar uma máquina, retorna.
        if (returnMessage != null){
            Utils.okPrint("O Seeker achou uma máquina!");
            sendMessage(requester, returnMessage);
            return;
        }else{
            // Se não, retorna negativo
            sendMessage(requester, new Message(self, "notfound", new String[] { }));
        }
    }
    
    // Inicializa o fluxo de procurar uma máquina
    private void seekMachine(String [] body) {
        this.requirements = body;
        run();
    }

    // Lê uma mensagem pedindo para procurar uma máquina
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
