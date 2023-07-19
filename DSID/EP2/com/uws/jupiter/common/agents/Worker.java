package com.uws.jupiter.common.agents;

import com.uws.jupiter.common.AgentLookup;
import com.uws.jupiter.common.Host;
import com.uws.jupiter.common.Message;
import com.uws.jupiter.common.Utils;
import com.uws.jupiter.common.apollo.Apollo;
import com.uws.jupiter.common.apollo.CodeResult;
import com.uws.jupiter.nameserver.LookupServer;

// Um agente que carrega código a ser rodado e retornado para a agência de origem
public class Worker extends Agent {
    private String payload;
    private String[] args;
    private CodeResult result;
    private AgentLookup self;
    private Host targetMachine;

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

    // Antes de sair, ele dá um tchauzinho, em breve ele estará de volta :)
    @Override
    public void beforeDeparture() {
        System.out.println(getName() + " saindo em uma aventura!");
        currentHost = targetMachine;
        goTo(currentHost);
    }

    // Assim que chega na agência estrangeira, ele roda o código em uma thread reservada pra ele
    @Override
    public void onArrival(Host host) {
        System.out.println("Olá " + host.getName() + "! " + self.getName() + " rodando aqui!");
        result = Apollo.runCode(payload, args);
    }

    // Assim que retorna, ele mostra na tela o resultado
    @Override
    public void onReturn() {
        // Retorna tudo oq ele calculou
        CodeResult result = getCodeResult();

        // Esse aqui é pra caso ele queira printar na tela
        if (result.hadErrors()) {
            Utils.failPrint("O código teve erros: ");
            Utils.bluePrint(result.getCompilationErrors());
        } else {
            Utils.okPrint("Logs de Execução do código: ");
            Utils.bluePrint(result.getLogs());
            Utils.okPrint("Retorno da Execução do código: ");
            Utils.bluePrint(result.getReturnValue());
        }
    }

    // Lê uma mensagem relatando se a máquina solicitada foi achada
    @Override
    public void readMessage(Message msg) {
        switch(msg.getMatter()){
            case "found": {
                Utils.okPrint("Máquina com os requerimentos pedidos foi achada! => " + msg.getBody()[0]);
                targetMachine = findMachineById(msg.getBody()[0]);
                run();
                break;
            }

            case "notfound": {
                Utils.failPrint("Não achamos uma máquina para rodar seu código! Mude os requerimentos.");
                break;
            }
        }
    }

    // Acha a agência com o Id passado pelo Seeker
    private Host findMachineById(String agencyName) {
        try {
            LookupServer ls = Utils.connectNameServer();
            return ls.searchAgency(agencyName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Roda o fluxo do worker
    public void sendAgent() {
        if(targetMachine != null){
            run();
        } else {
            Utils.failPrint("O agente não tem um host para rodar, procure um host antes!");
        }
    }

    // Manda uma mensagem para um seeker procurar uma máquina
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
    // =============================== GETTERS e SETTERS =============================== 

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
